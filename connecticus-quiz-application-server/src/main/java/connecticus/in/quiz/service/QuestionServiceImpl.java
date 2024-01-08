package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.StatusResponse;
import connecticus.in.quiz.exceptions.*;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.util.ExcelHelper;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ApiResponse saveAllQuestions(MultipartFile file) {
        int unique = 0, duplicate = 0;
        try {
            logger.info("Converting Excel to list of questions");
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            String sheetName = workbook.getSheetName(0);

            List<Question> questions = ExcelHelper.convertExcelToListOfQuestion(file.getInputStream(), sheetName);

            if (questions.isEmpty()) {
                throw new NoQuestionsFoundException("No questions found.");
            }

            logger.info("Saving all questions to the database");

            for (Question question : questions) {
                Optional<Question> existingQuestion = questionRepository.findByQuestion(question.getQuestion());
                if (existingQuestion.isEmpty()) {
                    unique++;
                    questionRepository.save(question);
                } else {
                    duplicate++;
                }
            }
        } catch (NoQuestionsFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while processing Excel file", e);
            throw new ExcelProcessingException("Data could not be stored");
        }

        String message;

        if (unique > 0) {
            message = "Successfully stored " + unique + " unique questions out of a total of " + (unique + duplicate) + ". Found " + duplicate + " duplicate questions.";
        } else {
            message = "Question set already present, please provide unique questions";
            return new ApiResponse(HttpStatus.BAD_REQUEST, message, "null");
        }

        return new ApiResponse(HttpStatus.OK, message, "null");
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

    @Override
    public ApiResponse deleteQuestion(DeleteRequest request) {
        logger.info("Deleting all requested exam");
        for (Integer id : request.getIdList()) {
            Optional<Question> question = questionRepository.findById(id);
            if (question.isPresent()) {
                questionRepository.deleteById(id);
            } else {
                logger.error("Error while database connection");
                throw new ServiceException("Question with id: " + id + " not found.");
            }

        }

        if (request.getIdList().size() == 1) {
            return new ApiResponse(HttpStatus.OK, "Successfully deleted question!", "Deleted question with id " + request.getIdList().toString());
        }
        return new ApiResponse(HttpStatus.OK, "Successfully deleted all questions!", "Deleted questions with id " + request.getIdList().toString());

    }

    @Override
    public ApiResponse updateQuestion(Integer id, Question question) {
        logger.info("Updating requested exam");
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            logger.error("Error while database connection");
            throw new ServiceException("Question with id " + id + " not found.");
        }

        Question updatedQuestion = new Question();

        updatedQuestion.setId(id);
        updatedQuestion.setQuestion(question.getQuestion());
        updatedQuestion.setSubject(question.getSubject());
        updatedQuestion.setDifficulty(question.getDifficulty());
        updatedQuestion.setType(question.getType());
        updatedQuestion.setOptions(question.getOptions());
        updatedQuestion.setAnswer(question.getAnswer());
        updatedQuestion.setActive(question.getActive());

        questionRepository.save(updatedQuestion);

        return new ApiResponse(HttpStatus.OK, "Successfully updated question", "null");

    }
}
