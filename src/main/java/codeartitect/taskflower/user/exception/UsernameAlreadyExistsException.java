package codeartitect.taskflower.user.exception;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException() {
        super("Username is already exists");
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
