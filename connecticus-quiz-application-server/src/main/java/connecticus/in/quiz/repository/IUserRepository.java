package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Role;
import connecticus.in.quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByRole(Role role);
}
