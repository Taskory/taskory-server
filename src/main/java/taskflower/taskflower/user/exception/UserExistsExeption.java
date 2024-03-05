package taskflower.taskflower.user.exception;

public class UserExistsExeption extends Exception {
    public UserExistsExeption() {
        super("Already existed user");
    }

    public UserExistsExeption(String message) {
        super(message);
    }
}
