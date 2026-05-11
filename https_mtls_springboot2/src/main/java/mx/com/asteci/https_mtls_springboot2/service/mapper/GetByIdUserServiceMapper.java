package mx.com.asteci.https_mtls_springboot2.service.mapper;

import mx.com.asteci.https_mtls_springboot2.model.response.Status;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetByIdResponse;
import org.springframework.stereotype.Component;

@Component
public class GetByIdUserServiceMapper {

    public UserGetByIdResponse provider_response_to_get_by_id_response(UserGetByIdResponse providerResponse) {
        Status status = Status.builder()
                .statusCode("200")
                .description("Usuario recuperado via cliente programatico mTLS")
                .build();
        return UserGetByIdResponse.builder()
                .status(status)
                .data(providerResponse.getData())
                .build();
    }
}
