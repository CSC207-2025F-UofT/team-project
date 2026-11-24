package data_access;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public final class SupabaseClient {
    private static final String URL_SETTING = "SUPABASE_URL";
    private static final String KEY_SETTING = "SUPABASE_PUBLISHABLE_KEY";
    private static final Pattern PROJECT_URL =
            Pattern.compile("https://[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.supabase\\.co/?");
    private static final Pattern PUBLISHABLE_KEY = Pattern.compile("sb_publishable_[A-Za-z0-9_-]+");
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(8);

    private final URI projectUrl;
    private final String publishableKey;
    private final HttpClient http;
    private final Duration requestTimeout;

    public static SupabaseClient fromEnvironment() {
        final Properties defaults = new Properties();
        try (InputStream resource = SupabaseClient.class.getResourceAsStream("/supabase.properties")) {
            if (resource != null) {
                defaults.load(resource);
            }
        } catch (IOException | IllegalArgumentException ignored) {
            throw new ConfigurationException("Cannot load the bundled Supabase settings.");
        }
        return fromConfiguration(System.getenv(), defaults);
    }

    static SupabaseClient fromConfiguration(Map<String, String> environment, Properties defaults) {
        final boolean urlOverridden = environment.containsKey(URL_SETTING);
        final boolean keyOverridden = environment.containsKey(KEY_SETTING);
        if (urlOverridden != keyOverridden) {
            final String missing = urlOverridden ? KEY_SETTING : URL_SETTING;
            throw new ConfigurationException("Set " + missing + " too, or use the bundled Supabase settings.");
        }
        final String url = environment.getOrDefault(URL_SETTING, defaults.getProperty(URL_SETTING, ""));
        final String key = environment.getOrDefault(KEY_SETTING, defaults.getProperty(KEY_SETTING, ""));
        if (!PROJECT_URL.matcher(url).matches()) {
            throw new ConfigurationException("SUPABASE_URL must be an HTTPS Supabase project origin.");
        }
        return new SupabaseClient(URI.create(url), key, CONNECT_TIMEOUT, REQUEST_TIMEOUT);
    }

    SupabaseClient(URI projectUrl, String publishableKey, Duration connectTimeout, Duration requestTimeout) {
        if (!PUBLISHABLE_KEY.matcher(publishableKey).matches()) {
            throw new ConfigurationException("SUPABASE_PUBLISHABLE_KEY must be a Supabase publishable key.");
        }
        this.projectUrl = projectUrl;
        this.publishableKey = publishableKey;
        this.requestTimeout = requestTimeout;
        this.http = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    HttpResponse<String> get(String path, String accessToken) throws IOException, InterruptedException {
        return send(request(path, accessToken).GET().build());
    }

    HttpResponse<String> post(String path, JsonObject body, String accessToken)
            throws IOException, InterruptedException {
        final HttpRequest.Builder request = request(path, accessToken);
        if (body == null) {
            return send(request.POST(HttpRequest.BodyPublishers.noBody()).build());
        }
        return send(request.header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build());
    }

    private HttpRequest.Builder request(String path, String accessToken) {
        final HttpRequest.Builder request = HttpRequest.newBuilder(projectUrl.resolve(path))
                .header("apikey", publishableKey)
                .header("Accept", "application/json");
        if (accessToken != null) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request;
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        // The deadline covers the entire exchange, including a response body that stalls after headers.
        final var exchange = http.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        try {
            return exchange.get(requestTimeout.toNanos(), TimeUnit.NANOSECONDS);
        } catch (TimeoutException ignored) {
            throw new HttpTimeoutException("Supabase request timed out.");
        } catch (ExecutionException exception) {
            if (exception.getCause() instanceof IOException failure) {
                throw failure;
            }
            throw new IOException("Supabase request failed.");
        } finally {
            exchange.cancel(true);
        }
    }

    public static final class ConfigurationException extends IllegalArgumentException {
        private ConfigurationException(String message) {
            super(message);
        }
    }
}
