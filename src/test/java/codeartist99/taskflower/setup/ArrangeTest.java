package codeartist99.taskflower.setup;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.event.EventRepository;
import codeartist99.taskflower.tag.TagRepository;
import codeartist99.taskflower.tag.model.Color;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.UserService;
import codeartist99.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
public class ArrangeTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EventRepository eventRepository;

    protected User tempUser;
    protected Tag tempTag;
    protected Event tempEvent;
    protected Task tempTask;

    @BeforeEach
    void setUp() {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        String username = tempUsername.toString();
        tempUser = User.builder()
                .username(username)
                .build();
        userRepository.save(tempUser);

        tempTag = tagRepository.save(
                Tag.builder()
                        .title("temporary tag")
                        .color(Color.NONE)
                        .user(tempUser)
                        .build());

        tempTask = taskRepository.save(
                Task.builder()
                        .title("temporary task title")
                        .status(Status.TO_DO)
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

    @AfterEach
    void tearDown() {
        if (tempUser != null) {
            userService.deleteById(tempUser.getId());
        }
    }
}
