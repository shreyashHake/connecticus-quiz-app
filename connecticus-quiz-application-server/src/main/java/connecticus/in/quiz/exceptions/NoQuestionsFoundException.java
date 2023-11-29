package connecticus.in.quiz.exceptions;

public class NoQuestionsFoundException extends RuntimeException {

    public NoQuestionsFoundException(String message) {
        super(message);
    }
}