package connecticus.in.quiz.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String question;
    private String subject;
    private String difficulty;
    private String type;
    @Column(columnDefinition = "TEXT")
    private String optionsJson;
    private String answer;
    private Boolean active = true;

    public Question() {
    }

    public Question(Integer id, String question, String subject, String difficulty, String type, String optionsJson, String answer, Boolean active) {
        this.id = id;
        this.question = question;
        this.subject = subject;
        this.difficulty = difficulty;
        this.type = type;
        this.optionsJson = optionsJson;
        this.answer = answer;
        this.active = active;
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
        return optionsJson != null ? Arrays.asList(optionsJson.split("##")) : new ArrayList<>();
    }

    public void setOptions(List<String> options) {
        this.optionsJson = options != null ? String.join("##", options) : null;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", subject='" + subject + '\'' +
                ", severity='" + difficulty + '\'' +
                ", type='" + type + '\'' +
                ", options=" + optionsJson +
                ", answer='" + answer + '\'' +
                '}';
    }
}
