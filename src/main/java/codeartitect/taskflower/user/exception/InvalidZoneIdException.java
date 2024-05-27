package codeartitect.taskflower.user.exception;


public class InvalidZoneIdException extends Exception {
    public InvalidZoneIdException() {
        super("Zone Id is Invalid");
    }

    public InvalidZoneIdException(String message) {
        super(message);
    }
}
