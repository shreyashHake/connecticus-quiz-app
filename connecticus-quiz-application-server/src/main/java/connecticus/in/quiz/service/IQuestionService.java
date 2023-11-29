package connecticus.in.quiz.service;

import connecticus.in.quiz.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IQuestionService {

    Page<Question> getAllQuestions(Pageable pageable);

    ResponseEntity<List<Question>> getAllQuestionsByDifficulty(String difficulty);

    ResponseEntity<List<Question>> getAllQuestionsBySubject(String subject);

    ResponseEntity<String> saveAllQuestions(MultipartFile file);

    List<String> getAllSubjects();

    List<String> getAllDifficulties();
}
