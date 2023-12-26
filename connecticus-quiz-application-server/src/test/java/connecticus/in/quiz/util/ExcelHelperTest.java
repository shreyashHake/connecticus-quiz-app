package connecticus.in.quiz.util;

import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.util.ExcelHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ExcelHelperTest {

    @TempDir
    Path tempDir;

    @Test
    void checkExcelFormat_Success() {
        // Arrange
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        MultipartFile file = createMockMultipartFile(contentType);

        // Act
        boolean result = ExcelHelper.checkExcelFormat(file);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkExcelFormat_Failure() {
        // Arrange
        String contentType = "application/pdf"; // Non-Excel content type
        MultipartFile file = createMockMultipartFile(contentType);

        // Act
        boolean result = ExcelHelper.checkExcelFormat(file);

        // Assert
        assertFalse(result);
    }

    @Test
    void convertExcelToListOfQuestion_EmptyStream_Failure() {
        // Arrange
        MultipartFile file = createMockMultipartFile("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Act & Assert
        assertThrows(ExcelProcessingException.class,
                () -> ExcelHelper.convertExcelToListOfQuestion(null, "Sheet1"));
    }

    @Test
    void convertExcelToListOfQuestion_NullSheetName_Failure() {
        // Arrange
        MultipartFile file = createMockExcelFile();

        // Act & Assert
        assertThrows(ExcelProcessingException.class,
                () -> ExcelHelper.convertExcelToListOfQuestion(getInputStream(file), null));
    }



    private MultipartFile createMockMultipartFile(String contentType) {
        return new MockMultipartFile("file", "test.xlsx", contentType, new byte[0]);
    }

    private MultipartFile createMockExcelFile() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Type");
        cell = headerRow.createCell(1);
        cell.setCellValue("Subject");
        cell = headerRow.createCell(2);
        cell.setCellValue("Difficulty");
        cell = headerRow.createCell(3);
        cell.setCellValue("Question");
        cell = headerRow.createCell(4);
        cell.setCellValue("Answer");
        Row dataRow = sheet.createRow(1);
        for (int i = 0; i < 5; i++) {
            dataRow.createCell(i).setCellValue("Data" + i);
        }
        return createMockMultipartFile("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private InputStream getInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get input stream from MultipartFile", e);
        }
    }
}
