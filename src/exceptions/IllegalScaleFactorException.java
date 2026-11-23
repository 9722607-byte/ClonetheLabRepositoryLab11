package exceptions;

public class IllegalScaleFactorException extends RuntimeException {
    public IllegalScaleFactorException() {
        super("Некоректний множник масштабування.");
    }

    public IllegalScaleFactorException(String message) {
        super(message);
    }
}