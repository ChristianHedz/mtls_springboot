package mx.com.asteci.https_mtls_springboot2.service;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot2.callback.rest.client.programmatic.UserProgrammaticRestClient;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetByIdResponse;
import mx.com.asteci.https_mtls_springboot2.service.mapper.GetByIdUserServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetByIdUserService {

    private final UserProgrammaticRestClient userProgrammaticRestClient;
    private final GetByIdUserServiceMapper getByIdUserServiceMapper;

    public ResponseEntity<UserGetByIdResponse> get_by_id_user_service_handler(Long id) {
        UserGetByIdResponse providerResponse = userProgrammaticRestClient.get_user_by_id(id);
        UserGetByIdResponse response = getByIdUserServiceMapper.provider_response_to_get_by_id_response(providerResponse);
        return ResponseEntity.ok(response);
    }
}
