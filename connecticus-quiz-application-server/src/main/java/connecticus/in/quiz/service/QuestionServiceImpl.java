package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.exceptions.ExcelProcessingException;
import connecticus.in.quiz.exceptions.NoDifficultiesFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.NoSubjectsFoundException;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.util.ExcelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements IQuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    private final IQuestionRepository questionRepository;

    public QuestionServiceImpl(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public String saveAllQuestions(MultipartFile file) {
        try {
            logger.info("Deleting all existing questions");
            this.questionRepository.deleteAll();

            logger.info("Converting Excel to list of questions");
            List<Question> questions = ExcelHelper.convertExcelToListOfQuestion(file.getInputStream(), "Sheet1");

            if (questions.isEmpty()) {
                throw new NoQuestionsFoundException("No questions found.");
            }

            logger.info("Saving all questions to the database");
            this.questionRepository.saveAll(questions);
        } catch (NoQuestionsFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while processing Excel file", e);
            throw new ExcelProcessingException("Data could not be stored");
        }

        return "Data has been stored";
    }

    @Override
    public Page<Question> getAllQuestions(Pageable pageable) {
        logger.info("Fetching all questions with pageable: {}", pageable);
        Page<Question> questions = questionRepository.findAll(pageable);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found.");
        }
        return questions;
    }

    @Override
    public List<Question> getAllQuestionsBySubject(String subject, int totalQuestions) {
        logger.info("Fetching {} questions with subject: {}", totalQuestions, subject);
        List<Question> questions = questionRepository.findAllBySubject(subject);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found with subject: " + subject);
        }
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(totalQuestions, questions.size()));
    }

    @Override
    public List<Question> getAllQuestionsByDifficulty(String difficulty, int totalQuestions) {
        logger.info("Fetching {} questions with difficulty: {}", totalQuestions, difficulty);
        List<Question> questions = questionRepository.findAllByDifficulty(difficulty);
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found with difficulty: " + difficulty);
        }
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(totalQuestions, questions.size()));
    }

    @Override
    public List<String> getAllSubjects() {
        logger.info("Fetching all subjects");
        List<String> subjects = questionRepository.findAllSubjects();
        if (subjects.isEmpty()) {
            throw new NoSubjectsFoundException("No subjects found.");
        }
        return subjects;
    }

    @Override
    public List<String> getAllDifficulties() {
        logger.info("Fetching all difficulties");
        List<String> difficulties = questionRepository.findAllDifficulties();
        if (difficulties.isEmpty()) {
            throw new NoDifficultiesFoundException("No difficulties found.");
        }
        return difficulties;
    }

    @Override
    public List<Question> getAllBySubjectAndDifficulty(String subject, String difficulty, int totalQuestion) {
        logger.info("Fetching {} questions with subject: {} and difficulty: {}", totalQuestion, subject, difficulty);
        List<Question> questions = questionRepository.findAllBySubjectAndDifficulty(subject, difficulty);
        if (questions == null || questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found.");
        }
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(totalQuestion, questions.size()));
    }

    @Override
    public StatusResponse changeStatus(int questionId) {
        logger.info("Changing status for question with ID: {}", questionId);
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found.");
        }
        Question question = questionOptional.get();

        Boolean status = question.getActive();
        question.setActive(!status);
        questionRepository.save(question);

        logger.info("Status changed successfully for question with ID: {}", questionId);
        return new StatusResponse(status, !status);
    }
}
