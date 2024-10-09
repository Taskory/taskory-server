package codeartist99.taskflower.task.model;

import codeartist99.taskflower.common.BaseTimeEntity;
import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Hashtag> hashtags = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskItem> items = new ArrayList<>();

    public void update(Task updateTask) {
        this.title = updateTask.getTitle();
        this.event = updateTask.getEvent();
        this.tag = updateTask.getTag();
        this.hashtags = updateTask.getHashtags();
        this.description = updateTask.getDescription();
        this.status = updateTask.getStatus();
    }
}
