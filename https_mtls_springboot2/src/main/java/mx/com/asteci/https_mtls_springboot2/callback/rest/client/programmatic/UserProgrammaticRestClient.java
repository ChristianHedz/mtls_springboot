package mx.com.asteci.https_mtls_springboot2.callback.rest.client.programmatic;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.asteci.https_mtls_springboot2.config.ProviderProperties;
import mx.com.asteci.https_mtls_springboot2.exception.UpstreamHttpException;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetByIdResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProgrammaticRestClient {

    private final HttpClient mtlsHttpClient;
    private final ProviderProperties providerProperties;
    private final ObjectMapper objectMapper;

    public UserGetByIdResponse get_user_by_id(Long id) {
        log.info("Calling provider GET /api/users/{} via programmatic client", id);
        return exchange("/api/users/" + id, UserGetByIdResponse.class);
    }

    private <T> T exchange(String path, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(providerProperties.getBaseUrl() + path))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = send(request);

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new UpstreamHttpException(status);
        }
        return deserialize(response.body(), responseType);
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return mtlsHttpClient.send(request, BodyHandlers.ofString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Provider call was interrupted", e);
        }
    }

    private <T> T deserialize(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
