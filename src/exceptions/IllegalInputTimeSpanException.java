package exceptions;

public class IllegalInputTimeSpanException extends RuntimeException {
    public IllegalInputTimeSpanException() {
        super("Некоректне значення вхідного інтервалу часу.");
    }

    public IllegalInputTimeSpanException(String message) {
        super(message);
    }
}