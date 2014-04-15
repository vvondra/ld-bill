package cz.vojtechvondra.ldbill.exceptions;

public class UnsupportedRowFormatException extends IllegalArgumentException {
    public UnsupportedRowFormatException(String msg) {
        super(msg);
    }
}
