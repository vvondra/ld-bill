package cz.vojtechvondra.ldbill.exceptions;

public class ConverterOutputException extends Exception {
    public ConverterOutputException(String message) {
        super(message);
    }

    public ConverterOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
