package connecticus.in.quiz.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoDifficultiesFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(NoDifficultiesFoundException.class);

    public NoDifficultiesFoundException(String message) {
        super(message);
        logger.error("NoDifficultiesFoundException: {}", message);
    }
}