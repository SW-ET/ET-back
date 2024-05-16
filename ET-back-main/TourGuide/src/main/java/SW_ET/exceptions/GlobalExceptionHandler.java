package SW_ET.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserServiceException(UserServiceException ex) {
        return ex.getMessage();
    }
}
