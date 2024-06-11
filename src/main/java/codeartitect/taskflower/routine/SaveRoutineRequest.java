package codeartitect.taskflower.routine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveRoutineRequest {
    private String title;
    private String description;
    private byte[] days;
}
