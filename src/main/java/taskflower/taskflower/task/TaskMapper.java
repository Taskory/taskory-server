package taskflower.taskflower.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "startTime", expression = "java(convertLocalDateTimeToArray(task.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(convertLocalDateTimeToArray(task.getEndTime()))")
    SaveTaskRequset convertTaskToSaveTaskRequest(Task task);

    @Mapping(target = "startTime", expression = "java(convertArrayToLocalDateTime(saveTaskRequest.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(convertArrayToLocalDateTime(saveTaskRequest.getEndTime()))")
    @Mapping(target = "createdTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Task convertSaveTaskRequestToTask(SaveTaskRequset saveTaskRequest);

    default Task updateTaskWithSaveTaskRequest(Task task, SaveTaskRequset saveTaskRequset) {
        LocalDateTime startTime = convertArrayToLocalDateTime(saveTaskRequset.getStartTime());
        LocalDateTime endTime = convertArrayToLocalDateTime(saveTaskRequset.getEndTime());

        task.setTitle(saveTaskRequset.getTitle());
        task.setDescription(saveTaskRequset.getDescription());
        task.setStatus(saveTaskRequset.getStatus());
        task.setTag(saveTaskRequset.getTag());
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setUpdatedTime(LocalDateTime.now());

        return task;
    }

     default LocalDateTime convertArrayToLocalDateTime(int[] array) {
        return LocalDateTime.of(array[0], array[1], array[2], array[3], array[4], 0, 0);
    }

    default int[] convertLocalDateTimeToArray(LocalDateTime localDateTime) {
        return new int[]{
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getSecond()
        };
    }
}
