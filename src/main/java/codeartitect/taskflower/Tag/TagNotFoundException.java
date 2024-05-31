package codeartitect.taskflower.Tag;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException() {
        super("Tag no found.");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
