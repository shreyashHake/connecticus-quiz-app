package connecticus.in.quiz.controller;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IQuestionService;
import connecticus.in.quiz.util.ExcelHelper;
import connecticus.in.quiz.util.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/question")
@CrossOrigin("*")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private final IQuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(IQuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> upload(@RequestParam("file") MultipartFile file) {
        logger.info("Received request to upload question from file: {}", file.getOriginalFilename());

        if (file == null || file.isEmpty() || !ExcelHelper.checkExcelFormat(file)) {
            logger.error("Invalid file received for question upload. File: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid file. Please upload a valid Excel file.", "uri=/question/upload"));
        }

        ApiResponse response = questionService.saveAllQuestions(file);
        logger.info("Question successfully uploaded from file: {}", file.getOriginalFilename());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/all/{page}/{size}")
    public ResponseEntity<Page<Question>> getAllQuestions(
            @PathVariable("page") int page,
            @PathVariable("size") int size
    ) {
        logger.info("Received request to fetch {} questions", size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getAllQuestions(pageable);

        logger.info("Question successfully fetched");
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/subject/{subject}/{totalQuestions}")
    public ResponseEntity<List<Question>> getAllQuestionsBySubject(
            @PathVariable("subject") String subject,
            @PathVariable("totalQuestions") int totalQuestions
    ) {
        logger.info("Received request to fetch: {} questions of subject: {}", totalQuestions, subject);
        List<Question> subjects = questionService.getAllQuestionsBySubject(subject, totalQuestions);
        logger.info("Question successfully fetched");
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/difficulty/{difficulty}/{totalQuestions}")
    public ResponseEntity<List<Question>> getAllQuestionsByDifficulty(
            @PathVariable("difficulty") String difficulty,
            @PathVariable("totalQuestions") int totalQuestions
    ) {
        logger.info("Received request to fetch: {} questions of difficulty: {}", totalQuestions, difficulty);
        List<Question> difficulties = questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions);
        logger.info("Question successfully fetched");
        return ResponseEntity.ok(difficulties);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects() {
        logger.info("Received request to fetch all subjects");
        List<String> subjects = questionService.getAllSubjects();
        logger.info("Subjects successfully fetched");
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/difficulties")
    public ResponseEntity<List<String>> getAllDifficulties() {
        logger.info("Received request to fetch all difficulties");
        List<String> subjects = questionService.getAllDifficulties();
        logger.info("Difficulties successfully fetched");
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/subjectAndDifficulty/{subject}/{difficulty}/{totalQuestions}")
    public ResponseEntity<List<Question>> getAllBySubjectAndDifficulty(
            @PathVariable("subject") String subject,
            @PathVariable("difficulty") String difficulty,
            @PathVariable("totalQuestions") int totalQuestions
    ) {
        logger.info("Received request to fetch: {} questions of subject: {} and difficulty: {}", totalQuestions, subject, difficulty);
        List<Question> questions = questionService.getAllBySubjectAndDifficulty(subject, difficulty, totalQuestions);
        logger.info("Question successfully fetched");
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/changeStatus/{questionId}")
    public ResponseEntity<StatusResponse> changeStatus(@PathVariable int questionId) {
        logger.info("Received request to change status for question with ID: {}", questionId);
        StatusResponse statusResponse = questionService.changeStatus(questionId);
        logger.info("Status changed successfully for question with ID: {}", questionId);
        return ResponseEntity.ok(statusResponse);
    }

    // Newly added
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody  DeleteRequest request) {
        logger.info("Received request to delete questions");
        ApiResponse response = questionService.deleteQuestion(request);
        logger.info("Successfully deleted questio");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> upldate(@PathVariable("id") Integer id, @RequestBody Question question) {
        logger.info("Received request to update question");
        ApiResponse response = questionService.updateQuestion(id, question);
        logger.info("Successfully updated question");
        return ResponseEntity.ok(response);
    }
}
