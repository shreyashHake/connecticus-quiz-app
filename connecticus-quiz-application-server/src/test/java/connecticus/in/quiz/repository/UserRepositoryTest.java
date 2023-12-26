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
    void findByEmailSuccess() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmailFailure() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.userDetailsService().loadUserByUsername(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByRoleSuccess() {
        Role role = Role.USER;
        User user = new User();
        user.setRole(role);
        when(userRepository.findByRole(role)).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByRole(role);

        assertTrue(result.isPresent());
        assertEquals(role, result.get().getRole());
        verify(userRepository, times(1)).findByRole(role);
    }

    @Test
    void findByRoleFailure() {
        Role role = Role.ADMIN;
        when(userRepository.findByRole(role)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByRole(role);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByRole(role);
    }

}
