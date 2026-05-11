package mx.com.asteci.https_mtls_springboot2.service;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot2.callback.rest.client.declarative.UserDeclarativeRestClient;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetAllResponse;
import mx.com.asteci.https_mtls_springboot2.service.mapper.GetAllUserServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUserService {

    private final UserDeclarativeRestClient userDeclarativeRestClient;
    private final GetAllUserServiceMapper getAllUserServiceMapper;

    public ResponseEntity<UserGetAllResponse> get_all_user_service_handler() {
        UserGetAllResponse providerResponse = userDeclarativeRestClient.get_all_users();
        UserGetAllResponse response = getAllUserServiceMapper.provider_response_to_get_all_response(providerResponse);
        return ResponseEntity.ok(response);
    }
}
