// IQuestionService.java
package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IQuestionService {

    Page<Question> getAllQuestions(Pageable pageable) throws NoQuestionsFoundException;

    List<Question> getAllQuestionsByDifficulty(String difficulty, int totalQuestions) throws NoQuestionsFoundException;

    List<Question> getAllQuestionsBySubject(String subject, int totalQuestions) throws NoQuestionsFoundException;

    ApiResponse saveAllQuestions(MultipartFile file) throws ExcelProcessingException;

    List<String> getAllSubjects() throws NoSubjectsFoundException;

    List<String> getAllDifficulties() throws NoDifficultiesFoundException;

    List<Question> getAllBySubjectAndDifficulty(String subject, String difficulty, int totalQuestion);

    StatusResponse changeStatus(int questionId);
}
