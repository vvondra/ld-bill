package cz.vojtechvondra.ldbill.exceptions;

/**
 * Thrown during processing of downloaded exports
 */
public class ConverterImportException extends Exception {
    public ConverterImportException(String message) {
        super(message);
    }

    public ConverterImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
