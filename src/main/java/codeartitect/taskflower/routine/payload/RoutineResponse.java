package codeartitect.taskflower.routine.payload;

import codeartitect.taskflower.routine.model.Routine;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RoutineResponse {
    private Long id;
    private String title;
    private String description;
    private boolean[] days;

    public RoutineResponse(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.description = routine.getDescription();
        this.days = routine.getDays();
    }
}
