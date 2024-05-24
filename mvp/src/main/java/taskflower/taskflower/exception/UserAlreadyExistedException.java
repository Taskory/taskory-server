package taskflower.taskflower.exception;

public class UserAlreadyExistedException extends Exception {
    public UserAlreadyExistedException() {
        super("Already existed user");
    }

    public UserAlreadyExistedException(String message) {
        super(message);
    }
}
