package taskflower.taskflower.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    @WithUserDetails(value = "test@test.test")      // Test userDetail(security test)
    @DisplayName("Task 생성 및 조화")
    void createTask() throws TaskNotFoundExeption {
        SaveTaskRequset saveTaskRequset = new SaveTaskRequset();
        saveTaskRequset.setTitle("test title");
        saveTaskRequset.setDescription("test description.....");
        saveTaskRequset.setStatus(Status.TODO);
        saveTaskRequset.setStartTime(new int[]{2024, 2, 16, 10, 15});
        saveTaskRequset.setEndTime(new int[]{2024, 3, 16, 10, 15});
        saveTaskRequset.setTag("test tag");

        Task savedTask = taskService.save(saveTaskRequset);

        System.out.println(savedTask.toString());

        Assertions.assertEquals(savedTask, taskService.getTaskById(savedTask.getId()));
    }


    @Test
    @WithUserDetails(value = "test@test.test")
    void updateTask() throws TaskNotFoundExeption {
        SaveTaskRequset saveTaskRequset = new SaveTaskRequset();
        saveTaskRequset.setTitle("test title");
        saveTaskRequset.setDescription("test description.....");
        saveTaskRequset.setStatus(Status.TODO);
        saveTaskRequset.setStartTime(new int[]{2024, 2, 16, 10, 15});
        saveTaskRequset.setEndTime(new int[]{2024, 3, 16, 10, 15});
        saveTaskRequset.setTag("test tag");

        Task savedTask = taskService.save(saveTaskRequset);

        SaveTaskRequset updateTaskRequset = new SaveTaskRequset();
        updateTaskRequset.setTitle("abcdefg");
        updateTaskRequset.setDescription("............description");
        updateTaskRequset.setStatus(Status.PROGRESS);
        updateTaskRequset.setStartTime(new int[]{2024, 2, 19, 10, 15});
        updateTaskRequset.setEndTime(new int[]{2024, 3, 11, 10, 15});
        updateTaskRequset.setTag("test tag");

        Task updatedTask = taskService.updateTask(savedTask.getId(), updateTaskRequset);

        Assertions.assertEquals(updatedTask, taskService.getTaskById(savedTask.getId()));

    }

}