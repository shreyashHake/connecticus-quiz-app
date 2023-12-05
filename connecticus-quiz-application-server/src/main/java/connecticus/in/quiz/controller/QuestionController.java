package connecticus.in.quiz.controller;

import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.service.IQuestionService;
import connecticus.in.quiz.util.ExcelHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/question")
@CrossOrigin("*")
public class QuestionController {
    private final IQuestionService questionService;

    public QuestionController(IQuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty() || !ExcelHelper.checkExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file. Please upload a valid Excel file.");
        }

        String result = questionService.saveAllQuestions(file);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/all")
    public ResponseEntity<Page<Question>> getAllQuestions(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getAllQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/subject")
    public ResponseEntity<List<Question>> getAllQuestionsBySubject(
            @RequestParam("subject") String subject,
            @RequestParam("totalQuestions") int totalQuestions
    ) {
        List<Question> subjects = questionService.getAllQuestionsBySubject(subject, totalQuestions);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/difficulty")
    public ResponseEntity<List<Question>> getAllQuestionsByDifficulty(
            @RequestParam("difficulty") String difficulty,
            @RequestParam("totalQuestions") int totalQuestions
    ) {
        List<Question> difficulties = questionService.getAllQuestionsByDifficulty(difficulty, totalQuestions);
        return ResponseEntity.ok(difficulties);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects() {
        List<String> subjects = questionService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/difficulties")
    public ResponseEntity<List<String>> getAllDifficulties() {
        List<String> subjects = questionService.getAllDifficulties();
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/subjectAndDifficulty")
    public ResponseEntity<List<Question>> getAllBySubjectAndDifficulty(
            @RequestParam("subject") String subject,
            @RequestParam("difficulty") String difficulty
    ) {
        List<Question> questions = questionService.getAllBySubjectAndDifficulty(subject, difficulty);
        return ResponseEntity.ok(questions);
    }

}
