package connecticus.in.quiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"examName", "subject", "questionCount", "marksPerQuestion", "duration", "status", "hardQuestions", "easyQuestions", "mediumQuestions"})
})

public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String examName;
    private String subject;
    private int questionCount;
    private int marksPerQuestion;
    private int duration;
    private String timeUnit;
    private boolean status;
    private boolean customDifficultyCount;
    private int hardQuestions;
    private int easyQuestions;
    private int mediumQuestions;


}
