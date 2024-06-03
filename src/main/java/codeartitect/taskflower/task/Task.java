package codeartitect.taskflower.task;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.global.BaseTimeEntity;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.taskitem.TaskItem;
import codeartitect.taskflower.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtags = new HashSet<>();

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskItem> items = new ArrayList<>();

    public Task(User user, SaveTaskRequest saveTaskRequest) {
        this.user = user;
        this.title = saveTaskRequest.getTitle();
        this.flow = saveTaskRequest.getFlow();
        this.event = saveTaskRequest.getEvent();
        this.tag = saveTaskRequest.getTag();
        setHashtags(saveTaskRequest.getHashtags());
        this.description = saveTaskRequest.getDescription();
        this.status = saveTaskRequest.getStatus();
    }

    private void setHashtags(Set<Hashtag> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) {
            this.hashtags = new HashSet<>();
        } else this.hashtags = hashtags;
    }

    @Override
    public ZoneId getUserZoneId() {
        return ZoneId.of(this.user.getZoneId());
    }

    public void update(SaveTaskRequest saveTaskRequest) {
        this.title = saveTaskRequest.getTitle();
        this.flow = saveTaskRequest.getFlow();
        this.event = saveTaskRequest.getEvent();
        this.tag = saveTaskRequest.getTag();
        setHashtags(saveTaskRequest.getHashtags());
        this.description = saveTaskRequest.getDescription();
        this.status = saveTaskRequest.getStatus();
    }
}
