package codearchitect99.taskory.routine.payload;

import codearchitect99.taskory.routine.model.RoutineHistory;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class RoutineHistoryResponse {
    private Long id;
    private Long routineId;
    private LocalDateTime checkedAt;
    public RoutineHistoryResponse(RoutineHistory routineHistory) {
        this.id = routineHistory.getId();
        this.routineId = routineHistory.getRoutine().getId();
        this.checkedAt = routineHistory.getCheckedAt();
    }
}
