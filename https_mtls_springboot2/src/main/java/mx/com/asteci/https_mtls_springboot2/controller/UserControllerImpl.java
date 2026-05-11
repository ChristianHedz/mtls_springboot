package mx.com.asteci.https_mtls_springboot2.controller;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetAllResponse;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetByIdResponse;
import mx.com.asteci.https_mtls_springboot2.service.GetAllUserService;
import mx.com.asteci.https_mtls_springboot2.service.GetByIdUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final GetAllUserService getAllUserService;
    private final GetByIdUserService getByIdUserService;

    @Override
    public ResponseEntity<UserGetAllResponse> getAll() {
        return getAllUserService.get_all_user_service_handler();
    }

    @Override
    public ResponseEntity<UserGetByIdResponse> getById(Long id) {
        return getByIdUserService.get_by_id_user_service_handler(id);
    }
}
