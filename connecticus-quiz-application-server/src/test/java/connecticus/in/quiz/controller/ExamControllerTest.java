package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.exceptions.NoExamFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IExamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamControllerTest {

    @Mock
    private IExamService examService;

    @InjectMocks
    private ExamController examController;

    @Test
    void generateExamSuccess() {
        List<Question> questions = Collections.singletonList(new Question());
        when(examService.generateExam()).thenReturn(questions);

        ResponseEntity<List<Question>> response = examController.generateExam();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(questions, response.getBody());
    }

    @Test
    void generateExamFailure() {
        when(examService.generateExam()).thenThrow(new NoQuestionsFoundException("No questions found"));

        assertThrows(NoQuestionsFoundException.class, () -> examController.generateExam());
    }

    @Test
    void createExamSuccess() {
        ExamRequest examRequest = new ExamRequest();
        when(examService.createExam(any(ExamRequest.class))).thenReturn(examRequest);

        ResponseEntity<ExamRequest> response = examController.createExam(examRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(examRequest, response.getBody());
    }

    @Test
    void updateExamSuccess() {
        int examId = 1;
        ExamRequest examRequest = new ExamRequest();
        when(examService.updateExam(eq(examId), any(ExamRequest.class))).thenReturn(examRequest);

        ResponseEntity<ExamRequest> response = examController.updateExam(examId, examRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(examRequest, response.getBody());
    }

    @Test
    void deleteExamSuccess() {
        int examId = 1;

        ResponseEntity<String> response = examController.deleteExam(examId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted exam with id: " + examId, response.getBody());
    }

    @Test
    void deleteExamFailure() {
        int examId = 1;
        doThrow(new NoExamFoundException("No exam found")).when(examService).deleteExamById(examId);

        assertThrows(NoExamFoundException.class, () -> examController.deleteExam(examId));
    }
}
