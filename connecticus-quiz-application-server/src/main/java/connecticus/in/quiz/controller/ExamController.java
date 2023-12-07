package connecticus.in.quiz.controller;

import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@RestController
@RequestMapping("/exam")

public class ExamController {
    @Autowired
    private IExamService examService;

    @GetMapping("/get")
    public ResponseEntity<List<Question>> generateExam(){
        List<Question> questions = examService.generateExam();
        return ResponseEntity.ok(questions);
    }
}
