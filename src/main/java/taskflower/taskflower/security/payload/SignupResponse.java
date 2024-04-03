package taskflower.taskflower.security.payload;

import lombok.Data;

@Data
public class SignupResponse {
    public SignupResponse(String message) {
        this.message = message;
    }

    private String message;
}
