package mx.com.asteci.https_mtls_springboot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HttpsMtlsSpringboot2Application {

    public static void main(String[] args) {
        SpringApplication.run(HttpsMtlsSpringboot2Application.class, args);
    }

}
