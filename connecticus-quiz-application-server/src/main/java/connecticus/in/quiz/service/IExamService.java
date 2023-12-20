package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.model.Question;

import java.util.List;

public interface IExamService {
    List<Question> generateExam();

    ExamRequest createExam(ExamRequest examRequest);

    void deleteExamById(int examId);
}
