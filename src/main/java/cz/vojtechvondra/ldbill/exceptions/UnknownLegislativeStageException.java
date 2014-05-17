package cz.vojtechvondra.ldbill.exceptions;

/**
 * Thrown when the downloaded export contains an unknown bill stage
 */
public class UnknownLegislativeStageException extends IllegalArgumentException {
    public UnknownLegislativeStageException(String message) {
        super(message);
    }
}
