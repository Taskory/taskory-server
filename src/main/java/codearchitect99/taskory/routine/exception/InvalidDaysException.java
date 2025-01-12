package codearchitect99.taskory.routine.exception;

public class InvalidDaysException extends Exception {
    public InvalidDaysException() {
        super("Invalid days. needs 7 days.");
    }

    public InvalidDaysException(String message) {
        super(message);
    }
}
