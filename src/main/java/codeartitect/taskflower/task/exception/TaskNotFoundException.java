package codeartitect.taskflower.task.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Your task is not found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
