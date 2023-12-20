// NoSubjectsFoundException.java
package connecticus.in.quiz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoSubjectsFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(NoSubjectsFoundException.class);

    public NoSubjectsFoundException(String message) {
        super(message);
        logger.error("NoSubjectsFoundException: {}", message);
    }
}
