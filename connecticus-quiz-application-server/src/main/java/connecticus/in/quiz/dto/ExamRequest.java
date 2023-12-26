package connecticus.in.quiz.dto;

import lombok.Data;

@Data
public class ExamRequest {
    private String examName;
    private int hardQuestions;
    private int easyQuestions;
    private int mediumQuestions;
    private String subject;
    // private int totalQuestions;
}
