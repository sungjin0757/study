package study.exception.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
}
