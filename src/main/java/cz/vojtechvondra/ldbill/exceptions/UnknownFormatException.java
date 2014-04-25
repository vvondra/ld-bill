package cz.vojtechvondra.ldbill.exceptions;

public class UnknownFormatException extends IllegalArgumentException {
    public UnknownFormatException(String message) {
        super(message);
    }
}
