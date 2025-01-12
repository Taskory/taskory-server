package codearchitect99.taskory.routine.service;

import codearchitect99.taskory.routine.exception.RoutineNotFoundException;
import codearchitect99.taskory.routine.model.Routine;
import codearchitect99.taskory.routine.model.RoutineHistory;
import codearchitect99.taskory.routine.payload.RoutineHistoryResponse;
import codearchitect99.taskory.routine.repository.RoutineHistoryRepository;
import codearchitect99.taskory.routine.repository.RoutineRepository;
import codearchitect99.taskory.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineHistoryService {

    private final RoutineRepository routineRepository;
    private final RoutineHistoryRepository routineHistoryRepository;

    @Autowired
    public RoutineHistoryService(RoutineRepository routineRepository, RoutineHistoryRepository routineHistoryRepository) {
        this.routineRepository = routineRepository;
        this.routineHistoryRepository = routineHistoryRepository;
    }


    /**
     * Check routine -> save history
     * @param user User information
     * @param routineId Routine id
     * @return RoutineHistoryResponse
     */
    public RoutineHistoryResponse checkRoutine(User user, Long routineId) throws RoutineNotFoundException {
        Routine routine = routineRepository.findById(routineId).orElseThrow(RoutineNotFoundException::new);
        RoutineHistory routineHistory = RoutineHistory.builder()
                .routine(routine)
                .user(user)
                .build();

        routineHistoryRepository.save(routineHistory);
        return new RoutineHistoryResponse(routineHistory);
    }

    /**
     * Uncheck routine -> delete history
     * @param historyId RoutineHistory id
     */
    public void uncheckRoutine(Long historyId) {
        routineHistoryRepository.deleteById(historyId);
    }
}
