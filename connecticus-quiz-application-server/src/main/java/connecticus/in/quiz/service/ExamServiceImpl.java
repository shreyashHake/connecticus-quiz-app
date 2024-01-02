package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.ChangeStatusRequest;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.exceptions.CustomBadRequest;
import connecticus.in.quiz.exceptions.NoExamFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.exceptions.ServiceException;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IExamRepository;
import connecticus.in.quiz.repository.IQuestionRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements IExamService {
    @Autowired
    private IExamRepository examRepository;

    @Autowired
    private IQuestionRepository questionRepository;

    @Autowired
    private IQuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Override
    public List<Question> generateExam() {
        logger.info("Generating exam");
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            logger.error("No Questions found");
            throw new NoQuestionsFoundException("No questions found with difficulty to create an Exam");
        }
        Collections.shuffle(questions);
        questions = questions.subList(0, 10);

        Exam exam = new Exam();

        examRepository.save(exam);
        return questions;
    }

    @Override
    public ExamRequest createExam(ExamRequest examRequest) {
        logger.info("Creating exam with examRequest");
        Exam exam = getExamFromRequest(examRequest);

        int hardQuestions = examRequest.getHardQuestions();
        int mediumQuestions = examRequest.getMediumQuestions();
        int easyQuestions = examRequest.getEasyQuestions();
        String subject = examRequest.getSubject();

        try {
            Optional<Exam> existingExam = examRepository.findExamByAttributes(
                    examRequest.getExamName(),
                    subject,
                    examRequest.getQuestionCount(),
                    examRequest.getMarksPerQuestion(),
                    examRequest.getDuration(),
                    examRequest.isStatus(),
                    hardQuestions,
                    easyQuestions,
                    mediumQuestions
            );

            if (existingExam.isPresent()) {
                throw new NoExamFoundException("Duplicate Exam found with same properties.");
            }

            validateExamRequest(examRequest);
            examRepository.save(exam);
        }  catch (DataIntegrityViolationException e) {
            logger.error("Error creating exam: {}", e.getMessage());
            throw new NoExamFoundException("Duplicate Exam found with same properties.");
        }

        return examRequest;
    }

    @Override
    public ExamRequest updateExam(int examId, ExamRequest examRequest) {
        logger.info("Updating exam using id and examRequest");
        Optional<Exam> examOptional = examRepository.findById(examId);
        if (examOptional.isEmpty()) {
            logger.error("No exam found with id: " + examId);
            throw new NoExamFoundException("No exam found with id: " + examId);
        }

        Exam exam = getExamFromRequest(examRequest);

        validateExamRequest(examRequest);
        exam.setId(examId);
        examRepository.save(exam);
        return examRequest;
    }

    private void validateExamRequest(ExamRequest examRequest) {
        logger.info("Validating exam request to check number of easy, medium and hard questions");
        int hardQuestions = examRequest.getHardQuestions();
        int mediumQuestions = examRequest.getMediumQuestions();
        int easyQuestions = examRequest.getEasyQuestions();
        String subject = examRequest.getSubject();

        int presentHard = questionRepository.findAllBySubjectAndDifficulty(subject, "Hard Level").size();
        if (presentHard < hardQuestions) {
            throw new CustomBadRequest("Inadequate hard questions, add "+ (hardQuestions - presentHard) +" hard questions to proceed");
        }

        int presentEasy = questionRepository.findAllBySubjectAndDifficulty(subject, "Easy Level").size();
        if (presentEasy < easyQuestions) {
            throw new CustomBadRequest("Inadequate easy questions, add "+ (easyQuestions - presentEasy) +" easy questions to proceed");
        }

        int presentMedium = questionRepository.findAllBySubjectAndDifficulty(subject, "Medium Level").size();
        if (presentMedium < mediumQuestions) {
            throw new CustomBadRequest("Inadequate medium questions, add "+ (mediumQuestions - presentMedium) +" medium questions to proceed");
        }
    }

    @Override
    public void deleteExamById(int examId) {
        logger.info("Deleting exam using id");
        Optional<Exam> exam = examRepository.findById(examId);
        if (exam.isEmpty()) {
            logger.error("No exam found");
            throw new NoExamFoundException("No exam found with id: " + examId);
        }
        examRepository.deleteById(examId);
    }

    @Override
    public List<Exam> getAllActiveExam() {
        logger.info("Getting all active exam");
        Optional<List<Exam>> examsOptional = examRepository.findAllByStatus(true);
        if (examsOptional.isEmpty()) {
            logger.error("No exam found");
            throw new NoExamFoundException("No exams found.");
        }
        return examsOptional.get();
    }

    @Override
    public List<Exam> getAllExam() {
        logger.info("Getting all active exam");
        List<Exam> exams = examRepository.findAll();
        if (exams.isEmpty()) {
            logger.error("No exam found");
            throw new NoExamFoundException("No exams found.");
        }
        return exams;
    }
    @Override
    public ApiResponse deleteExamsById(DeleteRequest idList) {
        logger.info("Deleting all requested exam");
        for (Integer examId : idList.getIdList()) {
            try {
                examRepository.deleteById(examId);
            } catch (Exception e) {
                logger.error("Error while database connection");
                throw new ServiceException("Database connection failed, Failed to delete exam with id: " + examId);
            }
        }
        return new ApiResponse(HttpStatus.OK, "Deleted all requested exams","Deleted exam with id: " + idList.getIdList().toString());
    }

    @Override
    public ApiResponse changeStatus(ChangeStatusRequest request) {
        logger.info("Changing status of all requested exam");
        boolean status = request.isStatus();

        for (Integer examId : request.getIdList()) {
            try {
                Exam exam = examRepository.findById(examId).get();
                exam.setStatus(status);

                examRepository.save(exam);
            } catch (Exception e) {
                logger.error("Error while database connection");
                throw new ServiceException("Database connection failed, Failed to delete exam with id: " + examId);
            }
        }
        return new ApiResponse(HttpStatus.OK, "All requested exam has status: " + (status? "Active" : "Inactive"),"Changed status of exams with id: " + request.getIdList().toString());
    }

    private List<Question> combineQuestions(List<Question> hard, List<Question> medium, List<Question> easy) {

        List<Question> combinedQuestions = new java.util.ArrayList<>(List.of());
        combinedQuestions.addAll(hard);
        combinedQuestions.addAll(medium);
        combinedQuestions.addAll(easy);
        return combinedQuestions;
    }

    private Exam getExamFromRequest(ExamRequest examRequest) {
        Exam exam = new Exam();

        exam.setExamName(examRequest.getExamName());
        exam.setQuestionCount(examRequest.getQuestionCount());
        exam.setSubject(examRequest.getSubject());
        exam.setMarksPerQuestion(examRequest.getMarksPerQuestion());
        exam.setDuration(examRequest.getDuration());
        exam.setTimeUnit(examRequest.getTimeUnit());
        exam.setStatus(examRequest.isStatus());
        exam.setCustomDifficultyCount(examRequest.isCustomDifficultyCount());

        if (examRequest.isCustomDifficultyCount()) {
            exam.setHardQuestions(examRequest.getHardQuestions());
            exam.setEasyQuestions(examRequest.getEasyQuestions());
            exam.setMediumQuestions(examRequest.getMediumQuestions());
        }

        return exam;
    }
}
