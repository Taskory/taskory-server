package codeartitect.taskflower.user.payload;

@Deprecated
public class SignupResponse {
    private String message;
    public SignupResponse(UserResponse userResponse) {
        this.message = "Welcome, " + userResponse.getUsername() + "! " +
                "Your registration was successful. " +
                "You can now log in using your credentials.";
    }

    public SignupResponse(String err) {
        this.message = err;
    }
}
