package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.JwtAuthenticationResponse;
import connecticus.in.quiz.dto.LoginRequest;
import connecticus.in.quiz.dto.RegisterRequest;
import connecticus.in.quiz.model.User;

public interface IAuthenticationService {
    User register(RegisterRequest registerRequest);

    JwtAuthenticationResponse login(LoginRequest loginRequest);
}
