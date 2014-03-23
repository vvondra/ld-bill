package cz.vojtechvondra.ldbill.exceptions;

public class UnknownLegislativeStageException extends IllegalArgumentException {
    public UnknownLegislativeStageException(String message) {
        super(message);
    }
}
