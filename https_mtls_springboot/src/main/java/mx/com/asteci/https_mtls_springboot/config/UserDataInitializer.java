package mx.com.asteci.https_mtls_springboot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.asteci.https_mtls_springboot.repository.UserRepository;
import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        List<User> users = userRepository.saveAll(List.of(
                User.builder().name("Cristian Hernández").email("cristian@asteci.mx").build(),
                User.builder().name("Ana López").email("ana@asteci.mx").build(),
                User.builder().name("Luis Martínez").email("luis@asteci.mx").build()
        ));
        log.info("Seeded {} users", users.size());
    }
}
