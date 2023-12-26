package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAll(Pageable pageable);

    List<Question> findAllByDifficulty(String difficulty);

    List<Question> findAllBySubject(String subject);

    @Query("SELECT DISTINCT q.subject FROM Question q WHERE q.subject IS NOT NULL AND q.subject <> ''")
    List<String> findAllSubjects();

    @Query("SELECT DISTINCT q.difficulty FROM Question q WHERE q.difficulty IS NOT NULL AND q.difficulty <> ''")
    List<String> findAllDifficulties();

    List<Question> findAllBySubjectAndDifficulty(String subject, String difficulty);
}
