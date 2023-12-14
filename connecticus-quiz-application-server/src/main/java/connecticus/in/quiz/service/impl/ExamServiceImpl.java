package connecticus.in.quiz.service.impl;

import connecticus.in.quiz.exceptions.NoQuestionsFoundException;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.Question;
import connecticus.in.quiz.repository.IExamRepository;
import connecticus.in.quiz.repository.IQuestionRepository;
import connecticus.in.quiz.service.IExamService;
import connecticus.in.quiz.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
