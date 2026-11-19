package data_access;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Pictogram;
import org.junit.jupiter.api.Test;
import use_case.customize_board.PictogramSearchException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArasaacDataAccessObjectTest {

    @Test
    void parsesSearchResultsIntoPictograms() throws Exception {
        String json = "["
                + "{\"_id\":2248,\"keywords\":[{\"keyword\":\"water\"}]},"
                + "{\"_id\":2462,\"keywords\":[{\"keyword\":\"apple\"}]}"
                + "]";

        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> response(200, json),
                        new ObjectMapper());

        List<Pictogram> results = dataAccess.searchPictograms("water");

        assertEquals(2, results.size());
        assertEquals(2248, results.get(0).getId());
        assertEquals("water", results.get(0).getLabel());
        assertEquals(
                "https://static.arasaac.org/pictograms/2248/2248_500.png",
                results.get(0).getImageLocation());
    }

    @Test
    void returnsEmptyListWhenNoPictogramsMatch() throws Exception {
        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> response(200, "[]"),
                        new ObjectMapper());

        assertTrue(dataAccess.searchPictograms("unknown").isEmpty());
    }

    @Test
    void reportsHttpFailure() {
        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> response(503, ""),
                        new ObjectMapper());

        PictogramSearchException exception = assertThrows(
                PictogramSearchException.class,
                () -> dataAccess.searchPictograms("water"));
        assertTrue(exception.getMessage().contains("503"));
    }

    @Test
    void reportsNetworkFailure() {
        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> {
                            throw new IOException("offline");
                        },
                        new ObjectMapper());

        assertThrows(
                PictogramSearchException.class,
                () -> dataAccess.searchPictograms("water"));
    }

    @Test
    void rejectsBlankKeyword() {
        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> response(200, "[]"),
                        new ObjectMapper());

        assertThrows(
                PictogramSearchException.class,
                () -> dataAccess.searchPictograms("   "));
    }

    @Test
    void reportsMalformedJson() {
        ArasaacDataAccessObject dataAccess =
                new ArasaacDataAccessObject(
                        request -> response(200, "{not-json"),
                        new ObjectMapper());

        assertThrows(
                PictogramSearchException.class,
                () -> dataAccess.searchPictograms("water"));
    }

    private static HttpResponse<String> response(int status, String body) {
        return new HttpResponse<>() {
            @Override
            public int statusCode() {
                return status;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return HttpHeaders.of(java.util.Map.of(), (name, value) -> true);
            }

            @Override
            public String body() {
                return body;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public java.net.http.HttpClient.Version version() {
                return java.net.http.HttpClient.Version.HTTP_1_1;
            }
        };
    }
}
