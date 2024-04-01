package taskflower.taskflower.task.exception;

public class TaskTitleExistException extends Exception {
    public TaskTitleExistException() {
        super("Task tile is already exists");
    }

    public TaskTitleExistException(String message) {
        super(message);
    }
}
