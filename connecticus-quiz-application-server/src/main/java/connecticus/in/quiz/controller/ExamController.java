package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.ChangeStatusRequest;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.model.Exam;
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
@CrossOrigin("*")
public class ExamController {
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    @Autowired
    private IExamService examService;

    @GetMapping("/get")
    public ResponseEntity<List<Question>> generateExam() {
        logger.info("Received request to get an exam.");
        List<Question> questions = examService.generateExam();
        logger.info("Exam created successfully..");
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/getActive")
    public ResponseEntity<List<Exam>> getAllActiveExam() {
        logger.info("Received request to get list of active exam.");
        List<Exam> exams = examService.getAllActiveExam();
        logger.info("Exam returned successfully..");
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/create")
    public ResponseEntity<ExamRequest> createExam(@RequestBody ExamRequest examRequest) {
        logger.info("Received request for creating exam: {}", examRequest.getExamName());
        ExamRequest exam = examService.createExam(examRequest);
        logger.info("Exam created successfully with name: {}", examRequest.getExamName());
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/update/{examId}")
    public ResponseEntity<ExamRequest> updateExam(@PathVariable int examId, @RequestBody ExamRequest examRequest) {
        logger.info("Received request for creating exam: {}", examRequest.getExamName());
        ExamRequest exam = examService.updateExam(examId,examRequest);
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

    // newly added :
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteExams(@RequestBody DeleteRequest idList) {
        logger.info("Received request to delete list of exam.");
        ApiResponse response = examService.deleteExamsById(idList);
        logger.info("Exam deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Exam>> getAllExam() {
        logger.info("Received request to get list exam.");
        List<Exam> exams = examService.getAllExam();
        logger.info("Exam returned successfully..");
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<ApiResponse> changeStatus(@RequestBody ChangeStatusRequest request) {
        logger.info("Received request to change status of multiple exam");
        ApiResponse response = examService.changeStatus(request);
        logger.info("Exam status changed successfully");
        return ResponseEntity.ok(response);
    }
}
