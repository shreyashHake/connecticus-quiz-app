package connecticus.in.quiz.service;

import connecticus.in.quiz.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepository userRepository;

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
}
