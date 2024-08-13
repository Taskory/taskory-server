package codeartist99.taskflower.hashtag;

public class HashtagNotFoundException extends Exception {
    public HashtagNotFoundException() {
        super("Hashtag not found.");
    }

    public HashtagNotFoundException(String message) {
        super(message);
    }
}
