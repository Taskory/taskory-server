package codeartitect.taskflower.event;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException() {
        super("Event not fouind");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
