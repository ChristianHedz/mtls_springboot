package mx.com.asteci.https_mtls_springboot2.config;

import lombok.RequiredArgsConstructor;
import mx.com.asteci.https_mtls_springboot2.callback.rest.client.programmatic.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    private final SslContextFactory sslContextFactory;

    @Bean
    public HttpClient mtlsHttpClient() throws GeneralSecurityException, IOException {
        return HttpClient.newBuilder()
                .sslContext(sslContextFactory.build())
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }
}
