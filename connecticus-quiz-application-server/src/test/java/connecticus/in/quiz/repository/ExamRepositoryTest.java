package connecticus.in.quiz.repository;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.exceptions.NoExamFoundException;
import connecticus.in.quiz.service.ExamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamRepositoryTest {

    @Mock
    private IExamRepository examRepository;

    @Mock
    private IQuestionRepository questionRepository;

    @InjectMocks
    private ExamServiceImpl examService;

    @Test
    public void testFindAllByStatusSuccess() {
        boolean status = true;
        List<Exam> expectedExams = Arrays.asList(new Exam(), new Exam());
        when(examRepository.findAllByStatus(status)).thenReturn(Optional.of(expectedExams));

        Optional<List<Exam>> result = Optional.ofNullable(examService.getAllActiveExam());

        assertTrue(result.isPresent());
        assertEquals(expectedExams.size(), result.get().size());
        verify(examRepository, times(1)).findAllByStatus(status);
    }

    @Test
    public void testFindAllByStatusFailure() {
        boolean status = true;
        when(examRepository.findAllByStatus(status)).thenReturn(Optional.empty());

        assertThrows(NoExamFoundException.class, () -> examService.getAllActiveExam());

        verify(examRepository, times(1)).findAllByStatus(status);
    }

    @Test
    public void testSaveExamSuccess() {
        ExamRequest examRequest = new ExamRequest();
        Exam expectedSavedExam = new Exam();
        when(examRepository.save(any(Exam.class))).thenReturn(expectedSavedExam);

        ExamRequest savedExam = examService.createExam(examRequest);

        assertNotNull(savedExam);
        assertEquals(expectedSavedExam.getExamName(), savedExam.getExamName());
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    public void testSaveExamFailure() {
        ExamRequest examRequest = new ExamRequest();
        when(examRepository.save(any(Exam.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(NoExamFoundException.class, () -> examService.createExam(examRequest));

        verify(examRepository, times(1)).save(any(Exam.class));
    }

}
