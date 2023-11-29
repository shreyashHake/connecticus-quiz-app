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
        if (ExcelHelper.checkExcelFormat(file)) {

            return questionService.saveAllQuestions(file);

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
    }

    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<Page<Question>> getAllQuestions(
            @PathVariable int page,
            @PathVariable int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getAllQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<Question>> getAllQuestionsBySubject(@PathVariable String subject) {
        return questionService.getAllQuestionsBySubject(subject);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Question>> getAllQuestionsByDifficulty(@PathVariable String difficulty) {
        return questionService.getAllQuestionsByDifficulty(difficulty);
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
}
