package edunhnil.project.forum.api.exception;

public class BadSqlException extends RuntimeException {
    public BadSqlException(String message) {
        super(message);
    }
}
