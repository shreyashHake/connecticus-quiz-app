package connecticus.in.quiz;

import connecticus.in.quiz.model.Role;
import connecticus.in.quiz.model.User;
import connecticus.in.quiz.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

/**
 * Author: Shreyash Hake
 * Organization: Connecticus Technologies
 */

@SpringBootApplication
@RequiredArgsConstructor
public class ConnecticusQuizApplication implements CommandLineRunner {

    private final IUserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(ConnecticusQuizApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> adminAccount = userRepository.findByRole(Role.ADMIN);

        if (adminAccount.isEmpty()) {
            User user = new User();

            user.setFirstName("admin");
            user.setLastName("admin");
            user.setEmail("admin@ctpl.in");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setRole(Role.ADMIN);

            userRepository.save(user);
        }
    }
}
