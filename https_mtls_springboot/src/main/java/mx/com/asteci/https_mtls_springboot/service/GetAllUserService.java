package mx.com.asteci.https_mtls_springboot.service;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot.model.response.UserGetAllResponse;
import mx.com.asteci.https_mtls_springboot.repository.UserRepository;
import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import mx.com.asteci.https_mtls_springboot.service.mapper.GetAllUserServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class GetAllUserService {

    private final UserRepository userRepository;
    private final GetAllUserServiceMapper getAllUserServiceMapper;

    public ResponseEntity<UserGetAllResponse> get_all_user_service_handler() {
        List<User> users = userRepository.findAll();
        UserGetAllResponse response = getAllUserServiceMapper.user_entity_list_to_get_all_response(users);
        return ResponseEntity.ok(response);
    }
}
