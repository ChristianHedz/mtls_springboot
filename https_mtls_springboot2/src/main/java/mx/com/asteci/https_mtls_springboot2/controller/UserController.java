package mx.com.asteci.https_mtls_springboot2.controller;

import mx.com.asteci.https_mtls_springboot2.model.response.UserGetAllResponse;
import mx.com.asteci.https_mtls_springboot2.model.response.UserGetByIdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users")
public interface UserController {

    @GetMapping
    ResponseEntity<UserGetAllResponse> getAll();

    @GetMapping("/{id}")
    ResponseEntity<UserGetByIdResponse> getById(@PathVariable Long id);
}
