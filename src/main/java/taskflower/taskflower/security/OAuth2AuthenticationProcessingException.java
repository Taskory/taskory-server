package taskflower.taskflower.security;

public class OAuth2AuthenticationProcessingException extends Exception {
    public OAuth2AuthenticationProcessingException() {
        super("Sorry! This is not supported yet.");
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }

    public OAuth2AuthenticationProcessingException(Throwable cause) {
        super(cause);
    }
}
