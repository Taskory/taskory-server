package codearchitect99.taskory.event;

public class EventNotFoundException extends Exception{
    public EventNotFoundException() {
        super("Event not fouind");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
