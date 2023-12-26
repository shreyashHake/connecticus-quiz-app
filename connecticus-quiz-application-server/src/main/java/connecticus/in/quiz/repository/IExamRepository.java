package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Integer> {
}
