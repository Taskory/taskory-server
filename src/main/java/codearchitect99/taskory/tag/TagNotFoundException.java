package codearchitect99.taskory.tag;

public class TagNotFoundException extends Exception{
    public TagNotFoundException() {
        super("Tag no found.");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
