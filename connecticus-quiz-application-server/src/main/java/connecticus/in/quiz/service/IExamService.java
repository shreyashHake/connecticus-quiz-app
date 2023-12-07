package connecticus.in.quiz.service;

import connecticus.in.quiz.model.Question;

import java.util.List;

public interface IExamService {
    List<Question> generateExam();
}
