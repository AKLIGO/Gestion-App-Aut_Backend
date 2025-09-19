package inf.akligo.auth.authConfiguration.request;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Builder
@AllArgsConstructor
// @NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse{
    private String token;
}