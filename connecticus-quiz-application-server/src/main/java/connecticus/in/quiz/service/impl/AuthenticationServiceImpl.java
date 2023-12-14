package connecticus.in.quiz.service.impl;

import connecticus.in.quiz.dto.JwtAuthenticationResponse;
import connecticus.in.quiz.dto.LoginRequest;
import connecticus.in.quiz.dto.RegisterRequest;
import connecticus.in.quiz.model.Role;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.repository.IUserRepository;
import connecticus.in.quiz.service.IAuthenticationService;
import connecticus.in.quiz.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    public User register(RegisterRequest registerRequest) {
        User user = new User();

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword())
        );

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRole(user.getRole().name());
        return jwtAuthenticationResponse;
    }

}
