package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.UserResponse;
import connecticus.in.quiz.exceptions.ServiceException;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.repository.IUserRepository;
import connecticus.in.quiz.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepository userRepository;

    @Autowired
    private  UserMapper userMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                logger.info("Loading user by username: {}", username);
                UserDetails user = userRepository.findByEmail(username)
                        .orElseThrow(() -> {
                            logger.error("User not found with username: {}", username);
                            return new UsernameNotFoundException("User not found");
                        });
                logger.info("User loaded successfully: {}", user.getUsername());
                return user;
            }
        };
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        logger.info("Fetching all user");

        List<UserResponse> userResponses = users.stream()
                .filter(user -> !user.getEmail().equals("admin@ctpl.in"))
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
        logger.info("User fetched successfully");
        return userResponses;
    }

    @Override
    public ApiResponse deleteUserById(int userId) {
        logger.info("Deleting user with id :" + userId);
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            logger.error("Error while database connection");
            throw new ServiceException("Database connection failed");
        }
        logger.info("User deleted successfully");
        return new ApiResponse(HttpStatus.OK,"User deleted with id :" + userId,"uri=/user/delete/"+userId);
    }

    @Override
    public ApiResponse deleteUserById(DeleteRequest deleteUsers) {
        logger.info("Deleting multiple user with id::" + deleteUsers.getIdList().toString());

        for (Integer userId: deleteUsers.getIdList()) {
            try {
                userRepository.deleteById(userId);
            } catch (Exception e) {
                logger.error("Error while database connection");
                throw new ServiceException("Database connection failed");
            }
        }

        logger.info("User deleted successfully");
        return new ApiResponse(HttpStatus.OK,"User deleted with id :" + deleteUsers.getIdList().toString(),"uri=/user/delete/"+deleteUsers.getIdList().toString());
    }
}
