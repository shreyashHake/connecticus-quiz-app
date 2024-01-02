package connecticus.in.quiz.repository;

import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {
    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private IQuestionRepository questionRepository;

    @Test
    void findAllSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Question> mockQuestions = Arrays.asList(
                new Question(1, "Question 1", "Math", "Easy", "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(2, "Question 2", "Math", "Medium", "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(3, "Question 3", "Science", "Hard", "Type 1", "Option 1##Option 2", "Option 1", true)
        );
        Page<Question> mockQuestionPage = new PageImpl<>(mockQuestions, pageable, mockQuestions.size());
        when(questionRepository.findAll(pageable)).thenReturn(mockQuestionPage);

        Page<Question> result = questionService.getAllQuestions(pageable);

        assertNotNull(result);
        assertEquals(mockQuestions.size(), result.getContent().size());
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAllFailure() {
        Pageable pageable = Pageable.unpaged();
        when(questionRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestions(pageable));
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAllByDifficultySuccess() {
        String difficulty = "Easy";
        List<Question> mockQuestions = Arrays.asList(
                new Question(1, "Question 1", "Math", "Easy", "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(2, "Question 2", "Science", "Easy", "Type 1", "Option 1##Option 2", "Option 1", true)
        );
        when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsByDifficulty(difficulty, 10);

        assertNotNull(result);
        assertEquals(mockQuestions.size(), result.size());
        verify(questionRepository, times(1)).findAllByDifficulty(difficulty);
    }

    @Test
    void findAllByDifficultyFailure() {
        String difficulty = "Hard";
        when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(List.of());

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsByDifficulty(difficulty, 10));
        verify(questionRepository, times(1)).findAllByDifficulty(difficulty);
    }

    @Test
    void findAllBySubjectSuccess() {
        String subject = "Math";
        List<Question> mockQuestions = Arrays.asList(
                new Question(1, "Question 1", subject, "Easy", "Type 1", "Option 1##Option 2", "Option 1", true),
                new Question(2, "Question 2", subject, "Medium", "Type 1", "Option 1##Option 2", "Option 1", true)
        );
        when(questionRepository.findAllBySubject(subject)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsBySubject(subject, 10);

        assertNotNull(result);
        assertEquals(mockQuestions.size(), result.size());
        verify(questionRepository, times(1)).findAllBySubject(subject);
    }

    @Test
    void findAllBySubjectFailure() {
        String subject = "History";
        when(questionRepository.findAllBySubject(subject)).thenReturn(List.of());

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllQuestionsBySubject(subject, 10));
        verify(questionRepository, times(1)).findAllBySubject(subject);
    }

    @Test
    void findAllSubjectsSuccess() {
        List<String> mockSubjects = Arrays.asList("Math", "Science", "History");
        when(questionRepository.findAllSubjects()).thenReturn(mockSubjects);

        List<String> result = questionService.getAllSubjects();

        assertNotNull(result);
        assertEquals(mockSubjects.size(), result.size());
        verify(questionRepository, times(1)).findAllSubjects();
    }

    @Test
    void findAllSubjectsFailure() {
        when(questionRepository.findAllSubjects()).thenReturn(List.of());

        assertThrows(NoSubjectsFoundException.class,
                () -> questionService.getAllSubjects());
        verify(questionRepository, times(1)).findAllSubjects();
    }

    @Test
    void findAllDifficultiesSuccess() {
        List<String> mockDifficulties = Arrays.asList("Easy", "Medium", "Hard");
        when(questionRepository.findAllDifficulties()).thenReturn(mockDifficulties);

        List<String> result = questionService.getAllDifficulties();

        assertNotNull(result);
        assertEquals(mockDifficulties.size(), result.size());
        verify(questionRepository, times(1)).findAllDifficulties();
    }

    @Test
    void findAllDifficultiesFailure() {
        when(questionRepository.findAllDifficulties()).thenReturn(List.of());

        assertThrows(NoDifficultiesFoundException.class,
                () -> questionService.getAllDifficulties());
        verify(questionRepository, times(1)).findAllDifficulties();
    }

    @Test
    void findAllBySubjectAndDifficultySuccess() {
        String mockSubject = "Science";
        String mockDifficulty = "Medium";
        List<Question> mockQuestions = Arrays.asList(new Question(), new Question());
        when(questionRepository.findAllBySubjectAndDifficulty(mockSubject, mockDifficulty))
                .thenReturn(mockQuestions);

        List<Question> result = questionService.getAllBySubjectAndDifficulty(mockSubject, mockDifficulty, 5);

        assertNotNull(result);
        assertEquals(mockQuestions.size(), result.size());
        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(mockSubject, mockDifficulty);
    }

    @Test
    void findAllBySubjectAndDifficultyFailure() {
        String mockSubject = "Nonexistent";
        String mockDifficulty = "Hard";
        when(questionRepository.findAllBySubjectAndDifficulty(mockSubject, mockDifficulty))
                .thenReturn(List.of());

        assertThrows(NoQuestionsFoundException.class,
                () -> questionService.getAllBySubjectAndDifficulty(mockSubject, mockDifficulty, 5));
        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(mockSubject, mockDifficulty);
    }

}
