package data_access.api;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class RateLimitedHttpClient {
    private static final class HttpJob<T> {
        private final HttpRequest request;
        private final HttpResponse.BodyHandler<T> bodyHandler;
        private final CompletableFuture<HttpResponse<T>> done = new CompletableFuture<>();

        private HttpJob(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
            this.request = request;
            this.bodyHandler = bodyHandler;
        }

        private void execute(HttpClient httpClient) {
            try {
                HttpResponse<T> res = httpClient.send(request, bodyHandler);
                done.complete(res);

            } catch (Throwable t) {
                if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }

                done.completeExceptionally(t);
            }
        }
    }

    private final HttpClient httpClient;
    private final BlockingQueue<HttpJob<?>> queue;
    private final String processName;
    private final long rateLimitInterval;

    public RateLimitedHttpClient(String processName, long rateLimitInterval) {
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        queue = new LinkedBlockingQueue<>();

        this.processName = processName;
        this.rateLimitInterval = rateLimitInterval;

        spawnWorker();
    }

    private void spawnWorker() {
        Thread worker = new Thread(() -> {
            long nextAllowed = 0L;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    HttpJob<?> job = queue.take();

                    long waitMs = nextAllowed - System.currentTimeMillis();
                    if (waitMs > 0) Thread.sleep(waitMs);

                    job.execute(httpClient);

                    nextAllowed = System.currentTimeMillis() + rateLimitInterval;

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    break;
                }
            }
        }, processName);

        worker.setDaemon(true);
        worker.start();
    }

    public <T> HttpResponse<T> sendRateLimited(
        HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler
    ) throws IOException, InterruptedException {
        HttpJob<T> job = new HttpJob<>(request, bodyHandler);
        queue.put(job);

        try {
            return job.done.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw e;

        } catch (ExecutionException e) {
            if (e.getCause() instanceof InterruptedException ie) {
                Thread.currentThread().interrupt();

                throw ie;
            }

            throw new IOException(e.getCause());
        }
    }
}
