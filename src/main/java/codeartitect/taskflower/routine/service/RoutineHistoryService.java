package codeartitect.taskflower.routine.service;

import codeartitect.taskflower.routine.exception.RoutineNotFoundException;
import codeartitect.taskflower.routine.model.Routine;
import codeartitect.taskflower.routine.model.RoutineHistory;
import codeartitect.taskflower.routine.payload.RoutineHistoryResponse;
import codeartitect.taskflower.routine.repository.RoutineHistoryRepository;
import codeartitect.taskflower.routine.repository.RoutineRepository;
import codeartitect.taskflower.user.model.User;
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
