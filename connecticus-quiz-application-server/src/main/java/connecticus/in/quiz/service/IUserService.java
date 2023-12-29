package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService {
    UserDetailsService userDetailsService();

    List<UserResponse> getAllUser();

    ApiResponse deleteUserById(int userId);
    ApiResponse deleteUserById(DeleteRequest deleteUsers);
}
