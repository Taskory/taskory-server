package codeartitect.taskflower.routine.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveRoutineRequest {
    private String title;
    private String description;
    private boolean[] days;
}
