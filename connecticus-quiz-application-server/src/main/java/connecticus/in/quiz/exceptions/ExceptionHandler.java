package connecticus.in.quiz.exceptions;

import connecticus.in.quiz.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ExcelProcessingException.class, NoExamFoundException.class, ServiceException.class,NoSubjectsFoundException.class, NoDifficultiesFoundException.class, NoQuestionsFoundException.class})
    protected ResponseEntity<Object> handleCustomExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        logger.warn("Custom exception handled: {}", ex.getMessage(), ex);

        ApiResponse apiResponse = new ApiResponse(
                status,
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomBadRequest.class})
    protected ResponseEntity<Object> handleCustomExceptionsBadRequest(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        logger.warn("Custom exception handled: {}", ex.getMessage(), ex);

        ApiResponse apiResponse = new ApiResponse(
                status,
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }


    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        logger.warn("Custom exception handled: {}", ex.getMessage(), ex);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
}
