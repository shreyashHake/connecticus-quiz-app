package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")

public class ExamController {
    @Autowired
    private IExamService examService;
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

    @GetMapping("/get")
    public ResponseEntity<List<Question>> generateExam() {
        logger.info("Received request to get an exam.");
        List<Question> questions = examService.generateExam();
        logger.info("Exam created successfully..");
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/create")
    public ResponseEntity<ExamRequest> createExam(@RequestBody ExamRequest examRequest) {
        logger.info("Received request for creating exam: {}", examRequest.getExamName());
        ExamRequest exam = examService.createExam(examRequest);
        logger.info("Exam created successfully with name: {}", examRequest.getExamName());
        return ResponseEntity.ok(exam);
    }

    @DeleteMapping("/delete/{examId}")
    public ResponseEntity<String> deleteExam(@PathVariable("examId") int examId) {
        logger.info("Received request for deleting exam: {}", examId);
        examService.deleteExamById(examId);
        logger.info("Exam deleted successfully with id: {}", examId);
        return ResponseEntity.ok("Deleted exam with id: " + examId);
    }
}
