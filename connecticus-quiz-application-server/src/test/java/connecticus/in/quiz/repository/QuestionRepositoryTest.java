package connecticus.in.quiz.repository;

import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {

    @Mock
    private Pageable pageable;

    @Mock
    private IQuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void testFindAll() {
        Page<Question> mockPage = mock(Page.class);
        when(questionRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Question> result = questionService.getAllQuestions(pageable);

        assertEquals(mockPage, result);
        verify(questionRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindAllByDifficulty() {
        String difficulty = "Easy";
        int totalQuestions = 10;
        List<Question> mockQuestions = Collections.singletonList(new Question());
        when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions);

        assertEquals(mockQuestions, result);
        verify(questionRepository, times(1)).findAllByDifficulty(difficulty);
    }

    @Test
    void testFindAllBySubject() {
        String subject = "Java";
        int totalQuestions = 10;
        List<Question> mockQuestions = Collections.singletonList(new Question());
        when(questionRepository.findAllBySubject(subject)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllQuestionsBySubject(subject, totalQuestions);

        assertEquals(mockQuestions, result);
        verify(questionRepository, times(1)).findAllBySubject(subject);
    }

    @Test
    void testFindAllSubjects() {
        List<String> mockSubjects = Arrays.asList("Java", "Python");
        when(questionRepository.findAllSubjects()).thenReturn(mockSubjects);

        List<String> result = questionService.getAllSubjects();

        assertEquals(mockSubjects, result);
        verify(questionRepository, times(1)).findAllSubjects();
    }

    @Test
    void testFindAllDifficulties() {
        List<String> mockDifficulties = Arrays.asList("Easy", "Medium");
        when(questionRepository.findAllDifficulties()).thenReturn(mockDifficulties);

        List<String> result = questionService.getAllDifficulties();

        assertEquals(mockDifficulties, result);
        verify(questionRepository, times(1)).findAllDifficulties();
    }

    @Test
    void testFindAllBySubjectAndDifficulty() {
        String subject = "Java";
        String difficulty = "Easy";
        int totalQuestion = 10;
        List<Question> mockQuestions = Arrays.asList(new Question(), new Question());
        when(questionRepository.findAllBySubjectAndDifficulty(subject, difficulty)).thenReturn(mockQuestions);

        List<Question> result = questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestion);

        assertEquals(mockQuestions, result);
        verify(questionRepository, times(1)).findAllBySubjectAndDifficulty(subject, difficulty);
    }
}
