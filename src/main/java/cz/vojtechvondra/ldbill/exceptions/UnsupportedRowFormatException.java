package cz.vojtechvondra.ldbill.exceptions;

/**
 * Thrown when the downloaded export contains a line not supported by an entity
 */
public class UnsupportedRowFormatException extends IllegalArgumentException {
    public UnsupportedRowFormatException(String msg) {
        super(msg);
    }
}
