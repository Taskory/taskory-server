package codeartitect.taskflower.task.exception;

public class TaskItemNotFoundException extends Exception {

    public TaskItemNotFoundException() {
        super("Task item not found.");
    }

    public TaskItemNotFoundException(String message) {
        super(message);
    }
}
