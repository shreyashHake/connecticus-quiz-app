package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.UserResponse;
import connecticus.in.quiz.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

    @GetMapping("/get")
    ResponseEntity<List<UserResponse>> getAllUser() {
        logger.info("Received request fetch all user");
        List<UserResponse> userResponses = userService.getAllUser();
        logger.info("User successfully fetch ");
        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping("/delete/{userId}")
    ResponseEntity<ApiResponse> deleteUserById(@PathVariable("userId") int userId) {
        logger.info("Received request to delete user with id: " + userId);
        ApiResponse response = userService.deleteUserById(userId);
        logger.info("User successfully deleted");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete")
    ResponseEntity<ApiResponse> deleteUsers(@RequestBody DeleteRequest deleteUsers) {
        logger.info("Received request to delete multiple users" );
        ApiResponse response = userService.deleteUserById(deleteUsers);
        logger.info("Users successfully deleted");
        return ResponseEntity.ok(response);
    }
}
