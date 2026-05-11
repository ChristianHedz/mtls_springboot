package mx.com.asteci.https_mtls_springboot2.callback.rest.client.programmatic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.asteci.https_mtls_springboot2.config.ProviderProperties;
import mx.com.asteci.https_mtls_springboot2.config.ProviderProperties.KeystoreProps;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

@Component
@RequiredArgsConstructor
@Slf4j
public class SslContextFactory {

    private static final String TLS_PROTOCOL = "TLSv1.3";

    private final ProviderProperties props;
    private final ResourceLoader resourceLoader;

    public SSLContext build() throws GeneralSecurityException, IOException {
        KeyStore keyStore = loadKeyStore(props.getKeystore());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, props.getKeystore().getPassword().toCharArray());

        KeyStore trustStore = loadKeyStore(props.getTruststore());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        log.info("Initialized {} SSLContext for programmatic mTLS client", TLS_PROTOCOL);
        return sslContext;
    }

    private KeyStore loadKeyStore(KeystoreProps keystoreProps)
            throws GeneralSecurityException, IOException {
        KeyStore store = KeyStore.getInstance(keystoreProps.getType());
        try (InputStream in = resourceLoader.getResource(keystoreProps.getPath()).getInputStream()) {
            store.load(in, keystoreProps.getPassword().toCharArray());
        }
        return store;
    }
}
