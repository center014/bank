package view.exception;

public class NoAccountException extends RuntimeException{

    private String message;

    public NoAccountException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
