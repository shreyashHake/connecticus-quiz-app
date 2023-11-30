package connecticus.in.quiz.util;

import connecticus.in.quiz.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExcelHelperTest {

    @Mock
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckExcelFormat() {
        MockMultipartFile validFile = createMockMultipartFile("test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        assertTrue(ExcelHelper.checkExcelFormat(validFile));

        MockMultipartFile invalidFile = createMockMultipartFile("test.txt", "text/plain");

        assertFalse(ExcelHelper.checkExcelFormat(invalidFile));
    }

    @Test
    void testConvertExcelToListOfQuestion() throws IOException {
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);

        MockMultipartFile mockMultipartFile = createMockMultipartFile("test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        List<Question> questions = ExcelHelper.convertExcelToListOfQuestion(mockMultipartFile.getInputStream(), null);

        assertEquals(0, questions.size());
    }

    private MockMultipartFile createMockMultipartFile(String filename, String contentType) {
        return new MockMultipartFile(
                "file", filename, contentType, new byte[0]);
    }
}
