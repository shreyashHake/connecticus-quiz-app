package connecticus.in.quiz.controller;

import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IQuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuestionService questionService;

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, "content".getBytes());

        when(questionService.saveAllQuestions(any(MultipartFile.class))).thenReturn("Success");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/question/upload").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }


    @Test
    public void testGetAllQuestions() throws Exception {
        Page<Question> emptyPage = new PageImpl<>(Collections.emptyList());

        when(questionService.getAllQuestions(any())).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.post("/question/all")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllQuestionsBySubject() throws Exception {
        when(questionService.getAllQuestionsBySubject(any(String.class), any(int.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/question/subject")
                        .param("subject", "Math")
                        .param("totalQuestions", "10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllQuestionsByDifficulty() throws Exception {
        when(questionService.getAllQuestionsByDifficulty(any(String.class), any(int.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/question/difficulty")
                        .param("difficulty","Easy")
                        .param("totalQuestions", "10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllSubjects() throws Exception {
        when(questionService.getAllSubjects()).thenReturn(Collections.singletonList("Math"));

        mockMvc.perform(MockMvcRequestBuilders.get("/question/subjects"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllDifficulties() throws Exception {
        when(questionService.getAllDifficulties()).thenReturn(Collections.singletonList("Easy"));

        mockMvc.perform(MockMvcRequestBuilders.get("/question/difficulties"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testGetAllBySubjectAndDifficulty() throws Exception {
        Mockito.when(questionService.getAllBySubjectAndDifficulty(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/question/subjectAndDifficulty")
                        .param("subject", "Math")
                        .param("difficulty", "Easy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
