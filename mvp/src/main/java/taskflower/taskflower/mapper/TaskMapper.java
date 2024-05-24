package taskflower.taskflower.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskflower.taskflower.model.entity.Task;
import taskflower.taskflower.model.dto.TaskDto;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "startTime", expression = "java(convertLocalDateTimeToArray(task.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(convertLocalDateTimeToArray(task.getEndTime()))")
    TaskDto convertTaskToTaskDto(Task task);

    @Mapping(target = "startTime", expression = "java(convertArrayToLocalDateTime(saveTaskRequest.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(convertArrayToLocalDateTime(saveTaskRequest.getEndTime()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Task convertTaskDtoToTask(TaskDto saveTaskRequest);

    default Task updateTaskWithTaskDto(Task task, TaskDto taskDto) {
        LocalDateTime startTime = convertArrayToLocalDateTime(taskDto.getStartTime());
        LocalDateTime endTime = convertArrayToLocalDateTime(taskDto.getEndTime());

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskDto.getTaskStatus());
        task.setTags(taskDto.getTags());
        task.setStartTime(startTime);
        task.setEndTime(endTime);

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
