package mx.com.asteci.https_mtls_springboot2.service.mapper;

import mx.com.asteci.https_mtls_springboot2.model.response.Status;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetAllResponse;
import org.springframework.stereotype.Component;

@Component
public class GetAllUserServiceMapper {

    public UserGetAllResponse provider_response_to_get_all_response(UserGetAllResponse providerResponse) {
        Status status = Status.builder()
                .statusCode("200")
                .description("Usuarios recuperados via cliente declarativo mTLS")
                .build();
        return UserGetAllResponse.builder()
                .status(status)
                .data(providerResponse.getData())
                .build();
    }
}
