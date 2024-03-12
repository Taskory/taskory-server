package taskflower.taskflower.task.tag;

public class TagNotFoundException extends Exception {
    public TagNotFoundException(String tagNotFound) {
        super(tagNotFound);
    }

    public TagNotFoundException() {
        super("Tag not found Exception");
    }
}
