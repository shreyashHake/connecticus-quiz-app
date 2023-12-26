package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private IQuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void getAllQuestionsSuccess() {

        Pageable pageable = mock(Pageable.class);
        Page<Question> page = new PageImpl<>(Arrays.asList(new Question(), new Question()));
        when(questionRepository.findAll(pageable)).thenReturn(page);

        Page<Question> result = questionService.getAllQuestions(pageable);

        assertEquals(page, result);
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllQuestionsFailure() {
        Pageable pageable = mock(Pageable.class);
        when(questionRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(NoQuestionsFoundException.class, () -> questionService.getAllQuestions(pageable));
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllQuestionsBySubjectSuccess() {
        String subject = "Java";
        int totalQuestions = 5;
        List<Question> mockQuestions = Arrays.asList(
                new Question(), new Question(), new Question(), new Question(), new Question());
        Mockito.when(questionRepository.findAllBySubject(subject)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsBySubject(subject, totalQuestions);

        assertEquals(totalQuestions, result.size());
        Mockito.verify(questionRepository, Mockito.times(1)).findAllBySubject(subject);
    }

    @Test
    void getAllQuestionsBySubjectFailure() {
        String subject = "Python";
        int totalQuestions = 10;
        Mockito.when(questionRepository.findAllBySubject(subject)).thenReturn(List.of());

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsBySubject(subject, totalQuestions));
        Mockito.verify(questionRepository, Mockito.times(1)).findAllBySubject(subject);
    }

    @Test
    void getAllQuestionsByDifficultySuccess() {
        String difficulty = "Easy";
        int totalQuestions = 5;
        List<Question> mockQuestions = Arrays.asList(
                new Question(), new Question(), new Question(), new Question(), new Question());
        Mockito.when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions);
        assertEquals(totalQuestions, result.size());
        Mockito.verify(questionRepository, Mockito.times(1)).findAllByDifficulty(difficulty);
    }

    @Test
    void getAllQuestionsByDifficultyFailure() {
        String difficulty = "Hard";
        int totalQuestions = 10;
        Mockito.when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(List.of());

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions));
        Mockito.verify(questionRepository, Mockito.times(1)).findAllByDifficulty(difficulty);
    }


    @Test
    void getAllSubjectsSuccess() {
        List<String> mockSubjects = Arrays.asList("Java", "Python", "JavaScript");
        Mockito.when(questionRepository.findAllSubjects()).thenReturn(mockSubjects);

        List<String> result = questionService.getAllSubjects();

        assertEquals(mockSubjects, result);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllSubjects();
    }

    @Test
    void getAllSubjectsFailure() {
        Mockito.when(questionRepository.findAllSubjects()).thenReturn(List.of());

        assertThrows(NoSubjectsFoundException.class, questionService::getAllSubjects);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllSubjects();
    }

    @Test
    void getAllDifficultiesSuccess() {
        List<String> mockDifficulties = Arrays.asList("Easy", "Medium", "Hard");
        Mockito.when(questionRepository.findAllDifficulties()).thenReturn(mockDifficulties);

        List<String> result = questionService.getAllDifficulties();

        assertEquals(mockDifficulties, result);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllDifficulties();
    }

    @Test
    void getAllDifficultiesFailure() {
        Mockito.when(questionRepository.findAllDifficulties()).thenReturn(List.of());

        assertThrows(NoDifficultiesFoundException.class, questionService::getAllDifficulties);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllDifficulties();
    }

    @Test
    void getAllBySubjectAndDifficultySuccess() {
        String subject = "Math";
        String difficulty = "Easy";
        int totalQuestion = 5;

        List<Question> mockQuestions = Arrays.asList(
                new Question(1, "Question 1", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(2, "Question 2", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(3, "Question 3", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true)
        );

        IQuestionRepository questionRepository = mock(IQuestionRepository.class);
        when(questionRepository.findAllBySubjectAndDifficulty(subject, difficulty)).thenReturn(mockQuestions);

        QuestionServiceImpl questionService = new QuestionServiceImpl(questionRepository);

        List<Question> result = questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestion);

        assertNotNull(result);
        assertEquals(Math.min(totalQuestion, mockQuestions.size()), result.size());
        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(subject, difficulty);
    }

    @Test
    void getAllBySubjectAndDifficultyFailure() {
        String subject = "Math";
        String difficulty = "Easy";
        int totalQuestion = 5;

        IQuestionRepository questionRepository = mock(IQuestionRepository.class);
        when(questionRepository.findAllBySubjectAndDifficulty(subject, difficulty)).thenReturn(Collections.emptyList());

        QuestionServiceImpl questionService = new QuestionServiceImpl(questionRepository);

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestion));

        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(subject, difficulty);
    }

    @Test
    void changeStatusSuccess() {
        // Given
        int questionId = 1;
        Question question = new Question();
        question.setId(questionId);
        question.setActive(true);
        Optional<Question> questionOptional = Optional.of(question);
        when(questionRepository.findById(questionId)).thenReturn(questionOptional);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        // When
        StatusResponse result = questionService.changeStatus(questionId);

        // Then
        assertNotNull(result);
//        assertFalse(result.getPreviousStatus());
//        assertTrue(result.getNewStatus());
        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void changeStatusFailure() {
        int questionId = 1;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(NoQuestionsFoundException.class, () -> questionService.changeStatus(questionId));
        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void saveAllQuestionsFailure() throws IOException {
        MultipartFile file = new MockMultipartFile("test-file", "test.xlsx", "application/vnd.ms-excel", new byte[0]);
        doThrow(new NoQuestionsFoundException("No questions found.")).when(questionRepository).deleteAll();

        assertThrows(NoQuestionsFoundException.class, () -> questionService.saveAllQuestions(file));
        verify(questionRepository, times(1)).deleteAll();
        verify(questionRepository, never()).saveAll(anyList());
    }

}
