package taskflower.taskflower;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestResponse {

    private final String message;

    public BadRequestResponse(String message) {
        this.message = message;
    }
}
