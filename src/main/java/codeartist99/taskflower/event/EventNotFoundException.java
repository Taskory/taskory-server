package codeartist99.taskflower.event;

public class EventNotFoundException extends Exception{
    public EventNotFoundException() {
        super("Event not fouind");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
