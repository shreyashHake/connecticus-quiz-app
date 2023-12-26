// ExcelProcessingException.java
package connecticus.in.quiz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelProcessingException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ExcelProcessingException.class);

    public ExcelProcessingException(String message) {
        super(message);
        logger.error("ExcelProcessingException: {}", message);
    }
}
