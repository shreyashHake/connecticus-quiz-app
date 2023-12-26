package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.service.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    void saveAllQuestionsFailureNoQuestionsFound() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test-file", "test.xlsx", "application/vnd.ms-excel", new byte[0]);
        doThrow(new NoQuestionsFoundException("No questions found.")).when(questionRepository).deleteAll();

        // When
        // Then
        assertThrows(NoQuestionsFoundException.class, () -> questionService.saveAllQuestions(file));
        verify(questionRepository, times(1)).deleteAll();
        verify(questionRepository, never()).saveAll(anyList());
    }

    @Test
    void getAllQuestionsSuccess() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<Question> page = new PageImpl<>(Arrays.asList(new Question(), new Question()));
        when(questionRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Question> result = questionService.getAllQuestions(pageable);

        // Then
        assertEquals(page, result);
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllQuestionsFailureNoQuestionsFound() {
        // Given
        Pageable pageable = mock(Pageable.class);
        when(questionRepository.findAll(pageable)).thenReturn(Page.empty());

        // When
        // Then
        assertThrows(NoQuestionsFoundException.class, () -> questionService.getAllQuestions(pageable));
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllQuestionsBySubjectSuccess() {
        // Given
        String subject = "Java";
        int totalQuestions = 5;
        List<Question> mockQuestions = Arrays.asList(
                new Question(), new Question(), new Question(), new Question(), new Question());
        Mockito.when(questionRepository.findAllBySubject(subject)).thenReturn(mockQuestions);

        // When
        List<Question> result = questionService.getAllQuestionsBySubject(subject, totalQuestions);

        // Then
        assertEquals(totalQuestions, result.size());
        Mockito.verify(questionRepository, Mockito.times(1)).findAllBySubject(subject);
    }

    @Test
    void getAllQuestionsBySubjectNoQuestionsFound() {
        // Given
        String subject = "Python";
        int totalQuestions = 10;
        Mockito.when(questionRepository.findAllBySubject(subject)).thenReturn(List.of());

        // When and Then
        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsBySubject(subject, totalQuestions));
        Mockito.verify(questionRepository, Mockito.times(1)).findAllBySubject(subject);
    }

    @Test
    void getAllQuestionsByDifficultySuccess() {
        // Given
        String difficulty = "Easy";
        int totalQuestions = 5;
        List<Question> mockQuestions = Arrays.asList(
                new Question(), new Question(), new Question(), new Question(), new Question());
        Mockito.when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(mockQuestions);

        // When
        List<Question> result = questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions);

        // Then
        assertEquals(totalQuestions, result.size());
        Mockito.verify(questionRepository, Mockito.times(1)).findAllByDifficulty(difficulty);
    }

    @Test
    void getAllQuestionsByDifficultyNoQuestionsFound() {
        // Given
        String difficulty = "Hard";
        int totalQuestions = 10;
        Mockito.when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(List.of());

        // When and Then
        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions));
        Mockito.verify(questionRepository, Mockito.times(1)).findAllByDifficulty(difficulty);
    }


    @Test
    void getAllSubjectsSuccess() {
        // Given
        List<String> mockSubjects = Arrays.asList("Java", "Python", "JavaScript");
        Mockito.when(questionRepository.findAllSubjects()).thenReturn(mockSubjects);

        // When
        List<String> result = questionService.getAllSubjects();

        // Then
        assertEquals(mockSubjects, result);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllSubjects();
    }

    @Test
    void getAllSubjectsNoSubjectsFound() {
        // Given
        Mockito.when(questionRepository.findAllSubjects()).thenReturn(List.of());

        // When and Then
        assertThrows(NoSubjectsFoundException.class, questionService::getAllSubjects);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllSubjects();
    }

    @Test
    void getAllDifficultiesSuccess() {
        // Given
        List<String> mockDifficulties = Arrays.asList("Easy", "Medium", "Hard");
        Mockito.when(questionRepository.findAllDifficulties()).thenReturn(mockDifficulties);

        // When
        List<String> result = questionService.getAllDifficulties();

        // Then
        assertEquals(mockDifficulties, result);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllDifficulties();
    }

    @Test
    void getAllDifficultiesNoDifficultiesFound() {
        // Given
        Mockito.when(questionRepository.findAllDifficulties()).thenReturn(List.of());

        // When and Then
        assertThrows(NoDifficultiesFoundException.class, questionService::getAllDifficulties);
        Mockito.verify(questionRepository, Mockito.times(1)).findAllDifficulties();
    }

    @Test
    void getAllBySubjectAndDifficultySuccess() {
        // Given
        String subject = "Math";
        String difficulty = "Easy";
        int totalQuestion = 5;

        List<Question> mockQuestions = Arrays.asList(
                new Question(1, "Question 1", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(2, "Question 2", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(3, "Question 3", subject, difficulty, "Type 1", "Option 1##Option 2", "Option 1", true)
        );

        // Mocking the repository method
        IQuestionRepository questionRepository = mock(IQuestionRepository.class);
        when(questionRepository.findAllBySubjectAndDifficulty(subject, difficulty)).thenReturn(mockQuestions);

        // Creating the service with the mocked repository
        QuestionServiceImpl questionService = new QuestionServiceImpl(questionRepository);

        // When
        List<Question> result = questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestion);

        // Then
        assertNotNull(result);
        assertEquals(Math.min(totalQuestion, mockQuestions.size()), result.size());
        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(subject, difficulty);
    }

    @Test
    void getAllBySubjectAndDifficultyFailure() {
        // Given
        String subject = "Math";
        String difficulty = "Easy";
        int totalQuestion = 5;

        // Mocking the repository method to return an empty list
        IQuestionRepository questionRepository = mock(IQuestionRepository.class);
        when(questionRepository.findAllBySubjectAndDifficulty(subject, difficulty)).thenReturn(Collections.emptyList());

        // Creating the service with the mocked repository
        QuestionServiceImpl questionService = new QuestionServiceImpl(questionRepository);

        // When
        // Calling the service method that should throw an exception for an empty list
        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestion));

        // Then
        // Verify that the repository method was called
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
    void changeStatusFailureQuestionNotFound() {
        // Given
        int questionId = 1;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(NoQuestionsFoundException.class, () -> questionService.changeStatus(questionId));
        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).save(any(Question.class));
    }

}
