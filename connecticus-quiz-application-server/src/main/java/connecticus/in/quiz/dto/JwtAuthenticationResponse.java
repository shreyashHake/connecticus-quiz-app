package connecticus.in.quiz.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private String role;
    private String email;
    private String firstName;
}
