package mx.com.asteci.https_mtls_springboot2.callback.rest.client.declarative;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetAllResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeclarativeRestClient {

    private final RestClient mtlsRestClient;

    public UserGetAllResponse get_all_users() {
        log.info("Calling provider GET /api/users via declarative client");
        return mtlsRestClient.get()
                .uri("/api/users")
                .retrieve()
                .body(UserGetAllResponse.class);
    }
}
