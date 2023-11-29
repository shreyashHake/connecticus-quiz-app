// IQuestionService.java
package connecticus.in.quiz.service;

import connecticus.in.quiz.exceptions.*;
import connecticus.in.quiz.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {

    Page<Question> getAllQuestions(Pageable pageable) throws NoQuestionsFoundException;

    List<Question> getAllQuestionsByDifficulty(String difficulty) throws NoQuestionsFoundException;

    List<Question> getAllQuestionsBySubject(String subject) throws NoQuestionsFoundException;

    ResponseEntity<String> saveAllQuestions(MultipartFile file) throws ExcelProcessingException;

    List<String> getAllSubjects() throws NoSubjectsFoundException;

    List<String> getAllDifficulties() throws NoDifficultiesFoundException;

    List<Question> getAllBySubjectAndDifficulty(String subject, String difficulty);

}
