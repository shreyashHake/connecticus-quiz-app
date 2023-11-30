// QuestionServiceImpl.java
package connecticus.in.quiz.service.impl;

import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.service.IQuestionService;
import connecticus.in.quiz.util.ExcelHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionRepository questionRepository;

    public QuestionServiceImpl(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public String saveAllQuestions(MultipartFile file) {
        try {
            // this.questionRepository.deleteAll();

            List<Question> questions = ExcelHelper.convertExcelToListOfQuestion(file.getInputStream(), "Sheet1");

            if (questions.isEmpty()) {
                throw new NoQuestionsFoundException("No questions found.");
            }

            this.questionRepository.saveAll(questions);
        } catch (NoQuestionsFoundException e) {
            throw e;
        } catch (Exception e) {
            Logger logger = Logger.getLogger(ExcelHelper.class.getName());
            logger.log(Level.SEVERE, "Error occurred while processing Excel file", e);
            throw new ExcelProcessingException("Data could not be stored");
        }

        return "Data has been stored";
    }

    @Override
    public Page<Question> getAllQuestions(Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found.");
        }
        return questions;
    }

    @Override
    public List<Question> getAllQuestionsByDifficulty(String difficulty) {
        List<Question> questions = questionRepository.findAllByDifficulty(difficulty);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found with difficulty: " + difficulty);
        }
        return questions;
    }

    @Override
    public List<Question> getAllQuestionsBySubject(String subject) {
        List<Question> questions = questionRepository.findAllBySubject(subject);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found with subject: " + subject);
        }
        return questions;
    }

    @Override
    public List<String> getAllSubjects() {
        List<String> subjects = questionRepository.findAllSubjects();
        if (subjects.isEmpty()) {
            throw new NoSubjectsFoundException("No subjects found.");
        }
        return subjects;
    }

    @Override
    public List<String> getAllDifficulties() {
        List<String> difficulties = questionRepository.findAllDifficulties();
        if (difficulties.isEmpty()) {
            throw new NoDifficultiesFoundException("No difficulties found.");
        }
        return difficulties;
    }

    @Override
    public List<Question> getAllBySubjectAndDifficulty(String subject, String difficulty) {
        List<Question> questions = questionRepository.findAllBySubjectAndDifficulty(subject, difficulty);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found.");
        }
        return questions;
    }


}
