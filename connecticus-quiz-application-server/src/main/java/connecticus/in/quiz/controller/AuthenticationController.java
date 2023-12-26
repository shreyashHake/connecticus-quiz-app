package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.JwtAuthenticationResponse;
import connecticus.in.quiz.dto.LoginRequest;
import connecticus.in.quiz.dto.RegisterRequest;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Received registration request for email: {}", registerRequest.getEmail());
        User registeredUser = authenticationService.register(registerRequest);
        logger.info("User registered successfully with email: {}", registeredUser.getEmail());
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for email: {}", loginRequest.getEmail());
        JwtAuthenticationResponse authenticationResponse = authenticationService.login(loginRequest);
        logger.info("User with email {} logged in successfully.", loginRequest.getEmail());
        return ResponseEntity.ok(authenticationResponse);
    }

}


























