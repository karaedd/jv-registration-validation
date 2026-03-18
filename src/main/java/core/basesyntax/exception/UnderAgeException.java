package core.basesyntax.exception;

public class UnderAgeException extends RuntimeException {
    public UnderAgeException(String message) {
        super((message));
    }
}
