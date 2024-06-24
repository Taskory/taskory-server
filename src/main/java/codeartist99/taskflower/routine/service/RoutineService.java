package codeartist99.taskflower.routine.service;

import codeartist99.taskflower.routine.repository.RoutineRepository;
import codeartist99.taskflower.routine.exception.InvalidDaysException;
import codeartist99.taskflower.routine.exception.RoutineNotFoundException;
import codeartist99.taskflower.routine.model.Routine;
import codeartist99.taskflower.routine.payload.RoutineResponse;
import codeartist99.taskflower.routine.payload.SaveRoutineRequest;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public RoutineResponse getById(Long id) throws RoutineNotFoundException {
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
        for (Optional<Routine> routine : routines) {
            routine.ifPresent(value -> routineResponseList.add(new RoutineResponse(value)));
        }
        return routineResponseList;
    }

    /**
     * Find all today's routine
     * @param user User information
     * @return RoutineResponse list
     */
    public List<RoutineResponse> findAllToday(User user) {
        int today = LocalDateTime.now(ZoneId.of(user.getZoneId())).getDayOfWeek().getValue() - 1;

        List<Optional<Routine>> routines = routineRepository.findAllByUser(user);

        List<RoutineResponse> routineResponseList = new ArrayList<>();
        for (Optional<Routine> routine : routines) {
            if (routine.isPresent()) {
                if (routine.get().getDays()[today]) {
                    routineResponseList.add(new RoutineResponse(routine.get()));
                }
            }
        }
        return routineResponseList;
    }


    /**
     * Update routine
     * @param routineId tag id
     * @param saveRoutineRequest Information to update routine
     * @return RoutineResponse
     */
    public RoutineResponse updateRoutine(Long routineId, SaveRoutineRequest saveRoutineRequest) throws InvalidDaysException, RoutineNotFoundException {
        if (isInvalidDays(saveRoutineRequest.getDays())) {
            throw new InvalidDaysException();
        }
        Routine routine = routineRepository.findById(routineId).orElseThrow(RoutineNotFoundException::new);
        routine.update(saveRoutineRequest);

        Routine updateRoutine = routineRepository.save(routine);
        return new RoutineResponse(updateRoutine);
    }

    /**
     * Delete routine by routine id
     * @param id Routine id for delete
     */
    public void deleteById(Long id) throws RoutineNotFoundException {
        if (routineRepository.existsById(id)) {
            routineRepository.deleteById(id);
        } else throw new RoutineNotFoundException();
    }

    /**
     * Check invalid days length
     * @param days Days information
     * @return boolean
     */
    private static boolean isInvalidDays(boolean[] days) {
        return days.length != 7;
    }
}
