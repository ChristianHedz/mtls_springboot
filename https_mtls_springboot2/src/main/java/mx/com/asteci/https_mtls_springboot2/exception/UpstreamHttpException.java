package mx.com.asteci.https_mtls_springboot2.exception;

import lombok.Getter;

@Getter
public class UpstreamHttpException extends RuntimeException {

    private final int upstreamStatus;

    public UpstreamHttpException(int upstreamStatus) {
        super("Upstream returned HTTP " + upstreamStatus);
        this.upstreamStatus = upstreamStatus;
    }
}
