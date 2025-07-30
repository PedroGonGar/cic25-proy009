package es.cic.curso._5.proy009.exception;

public class ModificationSecurityException extends RuntimeException {
    public ModificationSecurityException() {
        super("Has tratado de modificar mediante creación");
    }

    public ModificationSecurityException(String message) {
        super(message);
    }

    public ModificationSecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
