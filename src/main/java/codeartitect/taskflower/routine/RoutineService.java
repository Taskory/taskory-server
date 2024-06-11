package codeartitect.taskflower.routine;

import codeartitect.taskflower.tag.TagNotFoundException;
import codeartitect.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {
    private final RoutineRepository routineRepository;

    @Autowired
    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    /**
     * Save routine
     * @param user User information
     * @param saveRoutineRequest Information to save routine
     * @return RoutineResponse
     */
    public RoutineResponse save(User user, SaveRoutineRequest saveRoutineRequest) throws InvalidDaysException {
        if (isInvalidDays(saveRoutineRequest.getDays())) {
            throw new InvalidDaysException();
        }
        Routine routine = new Routine(user, saveRoutineRequest);

        routineRepository.save(routine);

        return new RoutineResponse(routine);
    }

    /**
     * Get routine by routine id
     * @param id routine id
     * @return RoutineResponse
     */
    public RoutineResponse getById(Long id) {
        Routine routine = routineRepository.findById(id).orElseThrow(RoutineNotFoundException::new);
        return new RoutineResponse(routine);
    }

    /**
     * Find all routines by user info
     * @param user User information
     * @return RoutineResponse list
     */
    public List<RoutineResponse> findAll(User user) {
        List<Optional<Routine>> routines = routineRepository.findAllByUser(user);

        List<RoutineResponse> routineResponseList = new ArrayList<>();
        for (Optional<Routine> tag : routines) {
            tag.ifPresent(value -> routineResponseList.add(new RoutineResponse(value)));
        }
        return routineResponseList;
    }

    /**
     * Update routine
     * @param routineId tag id
     * @param saveRoutineRequest Information to update routine
     * @return RoutineResponse
     */
    public RoutineResponse updateRoutine(Long routineId, SaveRoutineRequest saveRoutineRequest) throws InvalidDaysException {
        if (isInvalidDays(saveRoutineRequest.getDays())) {
            throw new InvalidDaysException();
        }
        Routine routine = routineRepository.findById(routineId).orElseThrow(TagNotFoundException::new);
        routine.update(saveRoutineRequest);

        Routine updateRoutine = routineRepository.save(routine);
        return new RoutineResponse(updateRoutine);
    }

    /**
     * Delete routine by routine id
     * @param id Routine id for delete
     */
    public void deleteById(Long id) {
        if (routineRepository.existsById(id)) {
            routineRepository.deleteById(id);
        } else throw new RoutineNotFoundException();
    }

    /**
     * Check invalid days length
     * @param days Days information
     * @return boolean
     */
    private static boolean isInvalidDays(byte[] days) {
        return days.length != 7;
    }
}
