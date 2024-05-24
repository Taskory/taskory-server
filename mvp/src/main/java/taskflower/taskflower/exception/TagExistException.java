package taskflower.taskflower.exception;

public class TagExistException extends Exception {
    public TagExistException(String msg) {
        super(msg);
    }

    public TagExistException() {
        super("Tag name already exists");
    }
}
