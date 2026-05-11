package mx.com.asteci.https_mtls_springboot.service.mapper;

import mx.com.asteci.https_mtls_springboot.model.response.Status;
import mx.com.asteci.https_mtls_springboot.model.response.UserGetAllResponse;
import mx.com.asteci.https_mtls_springboot.model.response.UserResponse;
import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllUserServiceMapper {

    public UserGetAllResponse user_entity_list_to_get_all_response(List<User> users) {
        List<UserResponse> data = users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .toList();
        Status status = Status.builder()
                .statusCode("200")
                .description("Usuarios recuperados exitosamente")
                .build();
        return UserGetAllResponse.builder()
                .status(status)
                .data(data)
                .build();
    }
}
