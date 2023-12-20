package connecticus.in.quiz.service.impl;

import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.service.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class QuestionServiceImplTest {

    @Mock
    private IQuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;


    @Test
    void testSaveAllQuestions() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(null);

        when(questionRepository.saveAll(any())).thenReturn(Collections.emptyList());

        NoQuestionsFoundException exception = assertThrows(NoQuestionsFoundException.class, () -> questionService.saveAllQuestions(file));
        assertEquals("No questions found.", exception.getMessage());
    }

    @Test
    void testSaveAllQuestions_ExcelProcessingException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(IOException.class);

        ExcelProcessingException exception = assertThrows(ExcelProcessingException.class, () -> questionService.saveAllQuestions(file));
        assertEquals("Data could not be stored", exception.getMessage());
    }

    @Test
    void testGetAllQuestions() {
        when(questionRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        NoQuestionsFoundException exception = assertThrows(NoQuestionsFoundException.class, () -> questionService.getAllQuestions(Pageable.unpaged()));
        assertEquals("No questions found.", exception.getMessage());
    }

    @Test
    void testGetAllBySubjectAndDifficulty() {
        when(questionRepository.findAllBySubjectAndDifficulty(eq("Subject1"), eq("Easy"))).thenReturn(Collections.emptyList());

        NoQuestionsFoundException exception = assertThrows(NoQuestionsFoundException.class, () -> questionService.getAllBySubjectAndDifficulty("Subject1", "Easy", 10));
        assertEquals("No questions found.", exception.getMessage());
    }
}
