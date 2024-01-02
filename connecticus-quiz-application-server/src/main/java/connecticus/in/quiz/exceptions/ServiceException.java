package connecticus.in.quiz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceException extends RuntimeException{
    private static final Logger logger = LoggerFactory.getLogger(NoExamFoundException.class);

    public ServiceException(String message) {
        super(message);
        logger.error("NoExamFoundException: {}", message);
    }
}
