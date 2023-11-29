package connecticus.in.quiz.service.impl;

import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.service.IQuestionService;
import connecticus.in.quiz.util.ExcelHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionRepository questionRepository;

    public QuestionServiceImpl(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<Question> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<List<Question>> getAllQuestionsByDifficulty(String difficulty) {
        return new ResponseEntity<>(questionRepository.findAllByDifficulty(difficulty), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Question>> getAllQuestionsBySubject(String subject) {
        return new ResponseEntity<>(questionRepository.findAllBySubject(subject), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveAllQuestions(MultipartFile file) {
        try {
            this.questionRepository.deleteAll();
            List<Question> products = ExcelHelper.convertExcelToListOfQestion(file.getInputStream(), "Sheet1");
            this.questionRepository.saveAll(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Data has been stored", HttpStatus.OK);
    }

    @Override
    public List<String> getAllSubjects() {
        return questionRepository.findAllSubjects();
    }

    @Override
    public List<String> getAllDifficulties() {
        return questionRepository.findAllDifficulties();
    }


}
