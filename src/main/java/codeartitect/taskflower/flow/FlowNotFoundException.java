package codeartitect.taskflower.flow;

public class FlowNotFoundException extends RuntimeException {
    public FlowNotFoundException() {
        super("Flow not found");
    }

    public FlowNotFoundException(String message) {
        super(message);
    }
}
