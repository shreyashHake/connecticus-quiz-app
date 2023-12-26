package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.JwtAuthenticationResponse;
import connecticus.in.quiz.dto.LoginRequest;
import connecticus.in.quiz.dto.RegisterRequest;
import connecticus.in.quiz.model.Role;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    public User register(RegisterRequest registerRequest) {
        logger.info("Attempting to register user with email: {}", registerRequest.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());
        if (userOptional.isPresent()) {
            logger.error("User with email {} already exists, registration failed", registerRequest.getEmail());
            throw new RuntimeException("User already present");
        }

        User user = new User();

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        logger.info("User registered successfully: {}", user.getEmail());
        return userRepository.save(user);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        logger.info("Attempting login for user with email: {}", loginRequest.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword())
        );

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> {
            logger.error("Invalid username or password for user with email: {}", loginRequest.getEmail());
            return new IllegalArgumentException("Invalid username or password");
        });
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRole(user.getRole().name());
        jwtAuthenticationResponse.setEmail(user.getEmail());
        jwtAuthenticationResponse.setFirstName(user.getFirstName());

        logger.info("User with email {} logged in successfully.", loginRequest.getEmail());

        return jwtAuthenticationResponse;
    }

}
