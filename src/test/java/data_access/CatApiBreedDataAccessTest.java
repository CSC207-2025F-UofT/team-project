package data_access;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;

import use_case.manage_pet.BreedLookupInteractor;
import use_case.manage_pet.BreedReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CatApiBreedDataAccessTest {
    private static final String TEST_KEY = "example-key";
    private static final String CDN = "cdn2.thecatapi.com";
    private static final byte[] PHOTO = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aA2wAAAAASUVORK5CYII=");

    @Test
    void returnsTheExactBreedAndOnlySendsTheKeyToTheApi() {
        final List<StubConnection> requests = new ArrayList<>();
        final CatApiBreedDataAccess api = new CatApiBreedDataAccess(TEST_KEY, uri -> {
            final StubConnection response;
            if (CDN.equals(uri.getHost())) {
                response = new StubConnection(uri.toURL(), 200, PHOTO);
            } else {
                assertEquals("api.thecatapi.com", uri.getHost());
                assertEquals("/v1/breeds/search", uri.getPath());
                assertEquals("q=british+shorthair", uri.getRawQuery());
                response = json(uri, 200, """
                        [{"name":"British Longhair","temperament":"Calm"},
                         {"name":"British Shorthair","temperament":"Calm, Affectionate",
                          "image":{"url":"https://cdn2.thecatapi.com/images/reference.png"}}]
                        """);
            }
            requests.add(response);
            return response;
        });

        final BreedReference reference = new BreedLookupInteractor(api).lookup("  british shorthair  ").orElseThrow();
        assertEquals("British Shorthair", reference.getName());
        assertEquals("Calm, Affectionate", reference.getTemperament());
        assertArrayEquals(PHOTO, reference.getPhoto());
        assertEquals(2, requests.size());
        assertEquals(TEST_KEY, requests.get(0).getRequestProperty("x-api-key"));
        assertNull(requests.get(1).getRequestProperty("x-api-key"));
    }

    @Test
    void keepsTemperamentWhenTheReferencePhotoIsUnavailable() {
        final CatApiBreedDataAccess api = new CatApiBreedDataAccess(TEST_KEY, uri -> {
            if ("/v1/breeds/search".equals(uri.getPath())) {
                return json(uri, 200, """
                        [{"name":"Siamese","temperament":"Playful","reference_image_id":"ref-image"}]
                        """);
            }
            assertEquals("/v1/images/ref-image", uri.getPath());
            return json(uri, 404, "{}");
        });
        final BreedReference reference = api.findByName("Siamese").orElseThrow();
        assertEquals("Playful", reference.getTemperament());
        assertEquals(0, reference.getPhoto().length);
    }

    @Test
    void distinguishesAnUnknownBreedFromDeniedAccess() {
        final CatApiBreedDataAccess empty = new CatApiBreedDataAccess(null, uri -> json(uri, 200, "[]"));
        assertTrue(empty.findByName("Mixed").isEmpty());

        final CatApiBreedDataAccess denied = new CatApiBreedDataAccess(null, uri -> json(uri, 403, "{}"));
        final IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> denied.findByName("Siamese"));
        assertTrue(error.getMessage().contains("THE_CAT_API_KEY"));
    }

    @Test
    void validatesTheSearchBeforeMakingARequest() {
        final BreedLookupInteractor lookup = new BreedLookupInteractor(name -> fail("Unexpected request"));
        assertThrows(IllegalArgumentException.class, () -> lookup.lookup(" "));
        assertThrows(IllegalArgumentException.class, () -> lookup.lookup("a".repeat(31)));
    }

    private static StubConnection json(URI uri, int status, String body) throws MalformedURLException {
        return new StubConnection(uri.toURL(), status, body.getBytes(StandardCharsets.UTF_8));
    }

    private static class StubConnection extends HttpURLConnection {
        private final int status;
        private final byte[] body;

        StubConnection(URL url, int status, byte[] body) {
            super(url);
            this.status = status;
            this.body = body;
        }

        @Override
        public int getResponseCode() {
            return status;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(body);
        }

        @Override
        public void disconnect() {
            // No network connection to close.
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() {
            // The response is supplied by the test.
        }
    }
}
