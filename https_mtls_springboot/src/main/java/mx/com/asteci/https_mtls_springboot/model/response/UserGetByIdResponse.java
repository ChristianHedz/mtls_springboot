package mx.com.asteci.https_mtls_springboot.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetByIdResponse {

    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private UserResponse data;
}
