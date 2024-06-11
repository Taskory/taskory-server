package codeartitect.taskflower.routine;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RoutineResponse {
    private Long id;
    private String title;
    private String description;
    private byte[] days;

    public RoutineResponse(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.description = routine.getDescription();
        this.days = routine.getDays();
    }
}
