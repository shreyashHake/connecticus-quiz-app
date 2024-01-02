package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.exceptions.NoExamFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IExamRepository;
import connecticus.in.quiz.repository.IQuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    private IExamRepository examRepository;

    @Mock
    private IQuestionRepository questionRepository;

    @InjectMocks
    private ExamServiceImpl examService;

    @Test
    void generateExamSuccess() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            questions.add(new Question());
        }
        when(questionRepository.findAll()).thenReturn(questions);

        List<Question> result = examService.generateExam();

        assertEquals(10, result.size());
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void generateExamFailureNoQuestions() {
        when(questionRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NoQuestionsFoundException.class, () -> examService.generateExam());
        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    void createExamSuccess() {
        ExamRequest examRequest = new ExamRequest();
        Exam createdExam = new Exam();
        when(examRepository.save(any(Exam.class))).thenReturn(createdExam);

        ExamRequest result = examService.createExam(examRequest);

        assertNotNull(result);
        assertEquals(examRequest, result);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void createExamFailure() {
        ExamRequest examRequest = new ExamRequest();
        when(examRepository.save(any(Exam.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(NoExamFoundException.class, () -> examService.createExam(examRequest));

        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void deleteExamByIdSuccess() {
        int examId = 1;
        Exam exam = new Exam();
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        examService.deleteExamById(examId);

        verify(examRepository, times(1)).deleteById(examId);
    }

    @Test
    void deleteExamByIdFailure() {
        int examId = 1;
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        assertThrows(NoExamFoundException.class, () -> examService.deleteExamById(examId));
        verify(examRepository, never()).deleteById(examId);
    }

    @Test
    void updateExamSuccess() {
        int examId = 1;
        ExamRequest examRequest = new ExamRequest();
        Exam existingExam = new Exam();
        when(examRepository.findById(examId)).thenReturn(Optional.of(existingExam));
        when(examRepository.save(any(Exam.class))).thenReturn(existingExam);

        ExamRequest result = examService.updateExam(examId, examRequest);

        assertNotNull(result);
        assertEquals(examRequest, result);
        verify(examRepository, times(1)).findById(examId);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void updateExamFailure() {
        int examId = 1;
        ExamRequest examRequest = new ExamRequest();
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        assertThrows(NoExamFoundException.class, () -> examService.updateExam(examId, examRequest));

        verify(examRepository, times(1)).findById(examId);
        verify(examRepository, never()).save(any(Exam.class));
    }

}
