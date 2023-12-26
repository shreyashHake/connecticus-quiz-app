package connecticus.in.quiz.service;

import connecticus.in.quiz.model.User;
import connecticus.in.quiz.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Test
    void loadUserByUsername_UserFound_Success() {
        String username = "test@example.com";
        IUserRepository userRepository = mock(IUserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        User user = new User();
        user.setEmail(username);
        user.setPassword("password");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound_Failure() {
        String username = "nonexistent@example.com";
        IUserRepository userRepository = mock(IUserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        Executable executable = () -> userService.userDetailsService().loadUserByUsername(username);

        assertThrows(UsernameNotFoundException.class, executable);
    }
}