package tr.com.readingisgood.app.model.exception;

public class ApplicationException extends RuntimeException {

    private final String errorMsg;

    public ApplicationException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
