package mx.com.asteci.https_mtls_springboot2.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "provider")
public class ProviderProperties {

    private String baseUrl;
    private KeystoreProps keystore;
    private KeystoreProps truststore;

    @Data
    @NoArgsConstructor
    public static class KeystoreProps {
        private String path;
        private String password;
        private String type;
    }
}
