package connecticus.in.quiz.util;

import connecticus.in.quiz.model.Question;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelHelper {
    public ExcelHelper() {
    }

    //check that file is of excel type or not
    public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();
        assert contentType != null;
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    }

    public static List<Question> convertExcelToListOfQuestion(InputStream is, String sheetName) {
        List<Question> questionList = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            // Use the provided sheetName or fallback to the first sheet
            XSSFSheet sheet = workbook.getSheet(sheetName != null ? sheetName : workbook.getSheetName(0));

            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;

                Question question = new Question();
                List<String> options = new ArrayList<>();
                boolean isEmptyRow = true;

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
                            question.setOptions(options);
                            break;
                        default:
                            break;
                    }
                    cid++;
                }

                if (!isEmptyRow) {
                    questionList.add(question);
                }

            }

        } catch (Exception e) {
            Logger logger = Logger.getLogger(ExcelHelper.class.getName());
            logger.log(Level.SEVERE, "An error occurred while processing Excel file", e);
        }
        return questionList;
    }

}
