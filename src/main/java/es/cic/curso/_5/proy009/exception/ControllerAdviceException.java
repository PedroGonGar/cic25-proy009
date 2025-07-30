package es.cic.curso._5.proy009.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdviceException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArbolException.class)
    public String handleArbolNotFound(ArbolException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ModificationSecurityException.class)
    public String handleModificationSecurity(ModificationSecurityException ex) {
        return ex.getMessage();
    }
}
