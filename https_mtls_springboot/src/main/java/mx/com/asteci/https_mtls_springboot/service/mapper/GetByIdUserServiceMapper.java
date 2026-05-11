package mx.com.asteci.https_mtls_springboot.service.mapper;

import mx.com.asteci.https_mtls_springboot.model.response.Status;
import mx.com.asteci.https_mtls_springboot.model.response.UserGetByIdResponse;
import mx.com.asteci.https_mtls_springboot.model.response.UserResponse;
import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GetByIdUserServiceMapper {

    public UserGetByIdResponse user_entity_to_get_by_id_response(User user) {
        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        Status status = Status.builder()
                .statusCode("200")
                .description("Usuario recuperado exitosamente")
                .build();
        return UserGetByIdResponse.builder()
                .status(status)
                .data(data)
                .build();
    }
}
