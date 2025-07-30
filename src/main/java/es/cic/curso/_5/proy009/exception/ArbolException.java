package es.cic.curso._5.proy009.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArbolException extends RuntimeException {
    public ArbolException(long id) {
        super("Arbol con ID " + id + " no encontrado.");
    }
}
