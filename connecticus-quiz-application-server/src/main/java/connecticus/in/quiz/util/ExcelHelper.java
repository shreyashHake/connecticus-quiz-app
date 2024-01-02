package connecticus.in.quiz.util;

import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.model.Question;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        assert contentType != null;
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Question> convertExcelToListOfQuestion(InputStream is, String sheetName) {
        List<Question> questionList = new ArrayList<>();

        try {

            if (is == null) {
                logger.error("Input stream is null. Cannot process Excel file.");
                throw new ExcelProcessingException("Input stream is null. Cannot process Excel file.");
            }
            if (is.available() == 0) {
                throw new ExcelProcessingException("The supplied file is empty.");
            }

            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = getSheet(workbook, sheetName);

            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                Question question = new Question();
                List<String> options = new ArrayList<>();
                boolean isEmptyRow = true;

                int cid = 0;

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            question.setType(cell.getStringCellValue());
                            break;
                        case 1:
                            question.setSubject(cell.getStringCellValue());
                            break;
                        case 2:
                            question.setDifficulty(cell.getStringCellValue());
                            break;
                        case 3:
                            question.setQuestion(cell.getStringCellValue());
                            break;
                        case 4:
                            question.setAnswer(cell.getStringCellValue());
                            break;
                        case 5, 6, 7, 8:
                            String option = cell.getStringCellValue();
                            options.add(option);
                            if (!option.isEmpty()) {
                                isEmptyRow = false;
                            }
                            break;
                        default:
                            break;
                    }

                    cid++;
                }

                if (!isEmptyRow) {
                    question.setOptions(options);
                    questionList.add(question);
                }
            }
        } catch (ExcelProcessingException e) {
            throw e;
        } catch (IOException e) {
            logger.error("An error occurred while processing Excel file", e);
            throw new ExcelProcessingException("An error occurred while processing Excel file");
        }

        return questionList;
    }

    private static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName != null ? sheetName : workbook.getSheetName(0));

        if (sheet == null) {
            throw new ExcelProcessingException("Sheet with name '" + sheetName + "' not found.");
        }

        return sheet;
    }

}
