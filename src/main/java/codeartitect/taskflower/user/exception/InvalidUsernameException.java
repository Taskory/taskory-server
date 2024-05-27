package codeartitect.taskflower.user.exception;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException() {
        super("Username is already exists");
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}
