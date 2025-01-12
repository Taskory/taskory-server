package codearchitect99.taskory.task.exception;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException() {
        super("Your task is not found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
