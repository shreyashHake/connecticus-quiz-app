package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamRepository extends JpaRepository<Exam, Integer> {
}
