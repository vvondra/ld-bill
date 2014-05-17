package cz.vojtechvondra.ldbill.exceptions;

/**
 * Thrown when trying to write RDF data in an unsupported format
 */
public class UnknownFormatException extends IllegalArgumentException {
    public UnknownFormatException(String message) {
        super(message);
    }
}
