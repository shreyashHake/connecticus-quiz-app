package connecticus.in.quiz.controller;

import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IQuestionService;
import connecticus.in.quiz.util.QuestionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private IQuestionService questionService;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void uploadSuccess() throws IOException {
        when(questionService.saveAllQuestions(any())).thenReturn("Questions uploaded successfully");

        byte[] content = "test data".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);

        ResponseEntity<String> responseEntity = questionController.upload(file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Questions uploaded successfully", responseEntity.getBody());

        verify(questionService, times(1)).saveAllQuestions(any());
    }

    @Test
    void uploadFailureInvalidFile() throws IOException {
        byte[] content = "test data".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content);

        ResponseEntity<String> responseEntity = questionController.upload(file);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid file. Please upload a valid Excel file.", responseEntity.getBody());

        verify(questionService, never()).saveAllQuestions(any());
    }

    @Test
    void getAllQuestionsSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Question> questionPage = new PageImpl<>(Collections.singletonList(new Question()));
        when(questionService.getAllQuestions(pageable)).thenReturn(questionPage);

        ResponseEntity<Page<Question>> responseEntity = questionController.getAllQuestions(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(questionPage, responseEntity.getBody());

        verify(questionService, times(1)).getAllQuestions(pageable);
    }

    @Test
    void getAllQuestionsFailure() {
        when(questionService.getAllQuestions(any())).thenThrow(new NoQuestionsFoundException("Failed to fetch questions"));

        ResponseEntity<Page<Question>> responseEntity = questionController.getAllQuestions(0, 10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(questionService, times(1)).getAllQuestions(any());
    }
}
