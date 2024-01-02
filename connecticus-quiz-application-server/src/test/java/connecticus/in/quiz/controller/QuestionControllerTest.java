package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IQuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private IQuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void uploadSuccess() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-file.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "your-file-content".getBytes()
        );
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Success", "uri=/question/upload");
        when(questionService.saveAllQuestions(any())).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = questionController.upload(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void uploadFailure() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-file.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "your-file-content".getBytes()
        );
        when(questionService.saveAllQuestions(any())).thenReturn(new ApiResponse(HttpStatus.BAD_REQUEST, "Failure", "uri=/question/upload"));

        ResponseEntity<ApiResponse> response = questionController.upload(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  // Fix: Check for 400 BAD_REQUEST
        assertEquals("Failure", response.getBody().getMessage());
    }

    @Test
    void getAllQuestionsBySubjectSuccess() {
        List<Question> questions = Collections.singletonList(new Question());
        when(questionService.getAllQuestionsBySubject(anyString(), anyInt())).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getAllQuestionsBySubject("Math", 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getAllQuestionsFailure() {
        when(questionService.getAllQuestions(any())).thenThrow(new NoQuestionsFoundException("Failed to fetch questions"));

        ResponseEntity<Page<Question>> responseEntity = questionController.getAllQuestions(0, 10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

        verify(questionService, times(1)).getAllQuestions(any());
    }
}
