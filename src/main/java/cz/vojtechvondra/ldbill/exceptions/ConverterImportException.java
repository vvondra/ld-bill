package cz.vojtechvondra.ldbill.exceptions;

public class ConverterImportException extends Exception {
    public ConverterImportException(String message) {
        super(message);
    }

    public ConverterImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
