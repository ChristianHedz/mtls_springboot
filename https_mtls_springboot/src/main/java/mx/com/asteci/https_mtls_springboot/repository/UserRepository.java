package mx.com.asteci.https_mtls_springboot.repository;

import mx.com.asteci.https_mtls_springboot.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
