package connecticus.in.quiz.model;

import jakarta.persistence.*;

import java.util.List;

// [{question-desc, subject,severity,type,options:[option1,optio2,option3,option4],answer}]
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String question;
    private String subject;
    private String difficulty;
    private String type;
    @Lob
    @Column(columnDefinition = "BLOB")
    private List<String> options;
    private String answer;

    public Question() {
    }

    public Question(String question, String subject, String difficulty, String type, List<String> options, String answer) {
        this.question = question;
        this.subject = subject;
        this.difficulty = difficulty;
        this.type = type;
        this.options = options;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", subject='" + subject + '\'' +
                ", severity='" + difficulty + '\'' +
                ", type='" + type + '\'' +
                ", options=" + options +
                ", answer='" + answer + '\'' +
                '}';
    }
}
