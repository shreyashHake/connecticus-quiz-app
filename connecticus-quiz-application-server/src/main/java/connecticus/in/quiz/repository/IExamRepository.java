package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Integer> {
    @Query("SELECT e FROM Exam e WHERE e.examName = :examName AND e.subject = :subject AND e.questionCount = :questionCount AND e.marksPerQuestion = :marksPerQuestion AND e.duration = :duration AND e.status = :status AND e.hardQuestions = :hardQuestions AND e.easyQuestions = :easyQuestions AND e.mediumQuestions = :mediumQuestions")
    Optional<Exam> findExamByAttributes(
            @Param("examName") String examName,
            @Param("subject") String subject,
            @Param("questionCount") int questionCount,
            @Param("marksPerQuestion") int marksPerQuestion,
            @Param("duration") int duration,
            @Param("status") boolean status,
            @Param("hardQuestions") int hardQuestions,
            @Param("easyQuestions") int easyQuestions,
            @Param("mediumQuestions") int mediumQuestions
    );
    Optional<List<Exam>> findAllByStatus(boolean status);
}
