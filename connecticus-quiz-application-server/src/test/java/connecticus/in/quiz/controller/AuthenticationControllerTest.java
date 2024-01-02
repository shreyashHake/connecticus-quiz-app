package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.JwtAuthenticationResponse;
import connecticus.in.quiz.dto.LoginRequest;
import connecticus.in.quiz.dto.RegisterRequest;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.service.IAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private IAuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void registerSuccess() {
        RegisterRequest registerRequest = new RegisterRequest();
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(new User());

        ResponseEntity<User> response = authenticationController.register(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void registerFailure() {
        RegisterRequest registerRequest = new RegisterRequest();
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("Registration failed"));

        assertThrows(RuntimeException.class, () -> authenticationController.register(registerRequest));
    }

    @Test
    void loginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(new JwtAuthenticationResponse());

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    void loginFailure() {
        LoginRequest loginRequest = new LoginRequest();
        when(authenticationService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Login failed"));

        assertThrows(RuntimeException.class, () -> authenticationController.login(loginRequest));
    }

}
