package connecticus.in.quiz.util;

import connecticus.in.quiz.exceptions.ExcelProcessingException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExcelHelperTest {

    @TempDir
    Path tempDir;

    @Test
    void checkExcelFormatSuccess() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        MultipartFile file = createMockMultipartFile(contentType);

        boolean result = ExcelHelper.checkExcelFormat(file);

        assertTrue(result);
    }

    @Test
    void checkExcelFormatFailure() {
        String contentType = "application/pdf"; // Non-Excel content type
        MultipartFile file = createMockMultipartFile(contentType);

        boolean result = ExcelHelper.checkExcelFormat(file);

        assertFalse(result);
    }

    @Test
    void convertExcelToListOfQuestionEmptyStreamFailure() {
        MultipartFile file = createMockMultipartFile("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        assertThrows(ExcelProcessingException.class,
                () -> ExcelHelper.convertExcelToListOfQuestion(null, "Sheet1"));
    }

    @Test
    void convertExcelToListOfQuestion_NullSheetName_Failure() {
        MultipartFile file = createMockExcelFile();

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
