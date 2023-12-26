package connecticus.in.quiz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoQuestionsFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(NoQuestionsFoundException.class);

    public NoQuestionsFoundException(String message) {
        super(message);
        logger.error("NoQuestionsFoundException: {}", message);
    }

}