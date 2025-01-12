package codearchitect99.taskory.setup;

import codearchitect99.taskory.event.Event;
import codearchitect99.taskory.event.EventRepository;
import codearchitect99.taskory.tag.TagRepository;
import codearchitect99.taskory.tag.model.Color;
import codearchitect99.taskory.tag.model.Tag;
import codearchitect99.taskory.task.model.Status;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.task.repository.TaskRepository;
import codearchitect99.taskory.user.UserRepository;
import codearchitect99.taskory.user.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
public class ArrangeTest {

    protected static User tempUser;
    protected static Tag tempTag;
    protected static Tag tempTag2;
    protected static Event tempEvent;
    protected static Task tempTask;

    @BeforeAll
    static void setUp(
            @Autowired UserRepository userRepository,
            @Autowired TagRepository tagRepository,
            @Autowired TaskRepository taskRepository,
            @Autowired EventRepository eventRepository) {

        String username = getRandomUsername(userRepository);

        tempUser = userRepository.save(
                User.builder()
                        .username(username)
                        .build()
        );


        tempTag = tagRepository.save(
                Tag.builder()
                        .title("temporary tag")
                        .color(Color.BLUE)
                        .user(tempUser)
                        .build());

        tempTag2 = tagRepository.save(
                Tag.builder()
                        .title("temporary tag2")
                        .color(Color.RED)
                        .user(tempUser)
                        .build());

        tempTask = taskRepository.save(
                Task.builder()
                        .title("temporary task title")
                        .status(Status.TODO)
                        .tag(tempTag)
                        .user(tempUser)
                        .build()
        );

        tempEvent = eventRepository.save(
                Event.builder()
                        .title("temporary event title")
                        .tag(tempTag)
                        .startDateTime(LocalDateTime.now())
                        .dueDateTime(LocalDateTime.now().plusDays(1))
                        .user(tempUser)
                        .build()
        );
    }

    private static String getRandomUsername(UserRepository userRepository) {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        return tempUsername.toString();
    }

    @AfterAll
    static void tearDown(@Autowired UserRepository userRepository) {
        userRepository.deleteById(tempUser.getId());
    }
}
