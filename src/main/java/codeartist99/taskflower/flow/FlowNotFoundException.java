package codeartist99.taskflower.flow;

public class FlowNotFoundException extends Exception {
    public FlowNotFoundException() {
        super("Flow not found");
    }

    public FlowNotFoundException(String message) {
        super(message);
    }
}
