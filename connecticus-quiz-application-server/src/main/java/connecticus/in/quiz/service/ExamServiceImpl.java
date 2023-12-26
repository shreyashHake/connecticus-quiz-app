package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.exceptions.NoExamFoundException;
import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IExamRepository;
import connecticus.in.quiz.repository.IQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Question> generateExam() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            throw new NoQuestionsFoundException("No questions found with difficulty to create an Exam");
        }
        Collections.shuffle(questions);
        questions = questions.subList(0, 10);

        Exam exam = new Exam();
        exam.setQuestions(questions);

        examRepository.save(exam);
        return questions;
    }

    @Override
    public ExamRequest createExam(ExamRequest examRequest) {

        int hardQuestions = examRequest.getHardQuestions();
        int mediumQuestions = examRequest.getMediumQuestions();
        int easyQuestions = examRequest.getEasyQuestions();
        String subject = examRequest.getSubject();
        // int totalQuestions = examRequest.getTotalQuestions();
        int totalQuestions = easyQuestions + mediumQuestions + hardQuestions;

        List<Question> hardQuestionsList = questionService.getAllBySubjectAndDifficulty(subject, "Hard Level", hardQuestions);
        if (hardQuestionsList.size() < hardQuestions) {
            throw new NoQuestionsFoundException("Insufficient questions with difficulty: Hard");
        }

        List<Question> mediumQuestionsList = questionService.getAllBySubjectAndDifficulty(subject, "Medium Level", mediumQuestions);
        if (mediumQuestionsList.size() < mediumQuestions) {
            throw new NoQuestionsFoundException("Insufficient questions with difficulty: Medium");
        }

        List<Question> easyQuestionsList = questionService.getAllBySubjectAndDifficulty(subject, "Easy Level", easyQuestions);
        if (easyQuestionsList.size() < easyQuestions) {
            throw new NoQuestionsFoundException("Insufficient questions with difficulty: Easy");
        }

        List<Question> allQuestions = combineQuestions(hardQuestionsList, mediumQuestionsList, easyQuestionsList);
        Collections.shuffle(allQuestions);
        allQuestions = allQuestions.subList(0, Math.min(totalQuestions, allQuestions.size()));

        Exam exam = new Exam();
        exam.setQuestions(allQuestions);
        examRepository.save(exam);
        return examRequest;
    }

    @Override
    public void deleteExamById(int examId) {
        Optional<Exam> exam = examRepository.findById(examId);
        if (exam.isEmpty()) {
            throw new NoExamFoundException("No exam found with id: " + examId);
        }
        examRepository.deleteById(examId);
    }

    private List<Question> combineQuestions(List<Question> hard, List<Question> medium, List<Question> easy) {

        List<Question> combinedQuestions = new java.util.ArrayList<>(List.of());
        combinedQuestions.addAll(hard);
        combinedQuestions.addAll(medium);
        combinedQuestions.addAll(easy);
        return combinedQuestions;
    }
}
