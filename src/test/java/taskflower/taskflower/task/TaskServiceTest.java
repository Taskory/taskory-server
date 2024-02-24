package taskflower.taskflower.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.ZonedDateTime;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    @WithUserDetails(value = "test@test.test")
//    @WithMockUser(username = "test@test.test", password = "1234")
    void createTask() throws TaskNotFoundExeption {
        SaveTaskRequset saveTaskRequset = new SaveTaskRequset();
        saveTaskRequset.setTitle("test title");
        saveTaskRequset.setDescription("test description.....");
        saveTaskRequset.setStatus(Status.TODO);
        saveTaskRequset.setStartTime("2024-02-16T10:15:30 Asia/Seoul");
        saveTaskRequset.setEndTime("2024-03-16T10:15:30 Asia/Seoul");
        saveTaskRequset.setTag("test tag");

        Task savedTask = taskService.save(saveTaskRequset);

        System.out.println(savedTask.toString());

//        Assertions.assertEquals(savedTask, taskService.getTaskById(savedTask.getId()));

    }


    @Test
    void timeTest() {
        String date = "2024-02-16T10:15:30 Asia/Seoul";
        ZonedDateTime time = ZonedDateTime.parse(date);

        System.out.println(time.toString());
    }

}