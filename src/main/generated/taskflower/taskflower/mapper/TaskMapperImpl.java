package taskflower.taskflower.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskflower.taskflower.model.dto.TaskDto;
import taskflower.taskflower.model.entity.Tag;
import taskflower.taskflower.model.entity.Task;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-25T17:26:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto convertTaskToTaskDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setTaskStatus( task.getTaskStatus() );
        Set<Tag> set = task.getTags();
        if ( set != null ) {
            taskDto.setTags( new LinkedHashSet<Tag>( set ) );
        }

        taskDto.setStartTime( convertLocalDateTimeToArray(task.getStartTime()) );
        taskDto.setEndTime( convertLocalDateTimeToArray(task.getEndTime()) );

        return taskDto;
    }

    @Override
    public Task convertTaskDtoToTask(TaskDto saveTaskRequest) {
        if ( saveTaskRequest == null ) {
            return null;
        }

        Task task = new Task();

        task.setTitle( saveTaskRequest.getTitle() );
        task.setDescription( saveTaskRequest.getDescription() );
        task.setTaskStatus( saveTaskRequest.getTaskStatus() );
        Set<Tag> set = saveTaskRequest.getTags();
        if ( set != null ) {
            task.setTags( new LinkedHashSet<Tag>( set ) );
        }

        task.setStartTime( convertArrayToLocalDateTime(saveTaskRequest.getStartTime()) );
        task.setEndTime( convertArrayToLocalDateTime(saveTaskRequest.getEndTime()) );

        return task;
    }
}
