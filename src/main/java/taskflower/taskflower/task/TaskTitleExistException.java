package taskflower.taskflower.task;

public class TaskTitleExistException extends Exception {
    public TaskTitleExistException() {
        super("Task tile is already exists");
    }

    public TaskTitleExistException(String message) {
        super(message);
    }
}
