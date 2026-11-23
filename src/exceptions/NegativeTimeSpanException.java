package exceptions;

public class NegativeTimeSpanException extends RuntimeException {
    public NegativeTimeSpanException() {
        super("Значення інтервалу часу не може бути від'ємним.");
    }

    public NegativeTimeSpanException(String message) {
        super(message);
    }
}