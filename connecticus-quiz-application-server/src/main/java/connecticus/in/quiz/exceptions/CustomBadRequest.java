package connecticus.in.quiz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBadRequest extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(NoDifficultiesFoundException.class);

    public CustomBadRequest(String message) {
        super(message);
        logger.error("CustomBadRequest: {}", message);
    }
}