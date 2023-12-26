package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Role;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private IUserRepository userRepository;

    @Test
    void findByEmail_Success() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);

        // Assert
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.userDetailsService().loadUserByUsername(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByRole_Success() {
        // Arrange
        Role role = Role.USER;
        User user = new User();
        user.setRole(role);
        when(userRepository.findByRole(role)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userRepository.findByRole(role);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(role, result.get().getRole());
        verify(userRepository, times(1)).findByRole(role);
    }

    @Test
    void findByRole_UserNotFound() {
        // Arrange
        Role role = Role.ADMIN;
        when(userRepository.findByRole(role)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByRole(role);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByRole(role);
    }

}
