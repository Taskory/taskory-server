package codeartitect.taskflower.task;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Your task is not found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
