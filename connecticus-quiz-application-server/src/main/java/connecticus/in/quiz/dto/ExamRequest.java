package connecticus.in.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamRequest {
    private String examName;
    private int questionCount ;
    private String subject;
    private int marksPerQuestion;
    private int duration ;
    private String timeUnit;
    private boolean status ;
    private boolean customDifficultyCount ;
    private int hardQuestions ;
    private int easyQuestions ;
    private int mediumQuestions ;
}
