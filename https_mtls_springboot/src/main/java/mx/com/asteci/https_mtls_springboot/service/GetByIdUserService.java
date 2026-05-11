package mx.com.asteci.https_mtls_springboot.service;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot.exception.ResourceNotFoundException;
import mx.com.asteci.https_mtls_springboot.model.response.UserGetByIdResponse;
import mx.com.asteci.https_mtls_springboot.repository.UserRepository;
import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import mx.com.asteci.https_mtls_springboot.service.mapper.GetByIdUserServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class GetByIdUserService {

    private final UserRepository userRepository;
    private final GetByIdUserServiceMapper getByIdUserServiceMapper;

    public ResponseEntity<UserGetByIdResponse> get_by_id_user_service_handler(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
        UserGetByIdResponse response = getByIdUserServiceMapper.user_entity_to_get_by_id_response(user);
        return ResponseEntity.ok(response);
    }
}
