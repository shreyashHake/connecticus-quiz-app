package connecticus.in.quiz.dto;

import lombok.Data;

@Data
public class QuestionResponse {
    private Integer id;
    private String question;
    private String subject;
    private String difficulty;
    private String type;
    private Boolean active = true;
}
