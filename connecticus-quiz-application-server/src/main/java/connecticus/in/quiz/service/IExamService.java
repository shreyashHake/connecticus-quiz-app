package connecticus.in.quiz.service;

import connecticus.in.quiz.dto.ApiResponse;
import connecticus.in.quiz.dto.ChangeStatusRequest;
import connecticus.in.quiz.dto.DeleteRequest;
import connecticus.in.quiz.dto.ExamRequest;
import connecticus.in.quiz.model.Exam;
import connecticus.in.quiz.model.Question;

import java.util.List;

public interface IExamService {
    List<Question> generateExam();

    // updated one
    ExamRequest createExam(ExamRequest examRequest);

    void deleteExamById(int examId);

    List<Exam> getAllActiveExam();

    ExamRequest updateExam(int examId, ExamRequest examRequest);

    ApiResponse deleteExamsById(DeleteRequest idList);

    List<Exam> getAllExam();

    ApiResponse changeStatus(ChangeStatusRequest request);
}
