package codeartitect.taskflower.task.taskitem;

public class TaskItemNotFoundException extends RuntimeException {

    public TaskItemNotFoundException() {
        super("Task item not found.");
    }

    public TaskItemNotFoundException(String message) {
        super(message);
    }
}
