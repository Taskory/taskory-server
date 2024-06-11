package codeartitect.taskflower.task.model;

import codeartitect.taskflower.tag.model.Tag;
import codeartitect.taskflower.common.EntityUtil;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.common.BaseTimeEntity;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.payload.SaveTaskRequest;
import codeartitect.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Task")
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

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flow_id")
    private Flow flow;

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

    public Task(User user, SaveTaskRequest saveTaskRequest) {
        this.user = user;
        this.title = saveTaskRequest.getTitle();
        this.flow = saveTaskRequest.getFlow();
        this.event = saveTaskRequest.getEvent();
        this.tag = saveTaskRequest.getTag();
        this.hashtags = EntityUtil.setListElements(saveTaskRequest.getHashtags());
        this.description = saveTaskRequest.getDescription();
        this.status = saveTaskRequest.getStatus();
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
        this.hashtags = EntityUtil.setListElements(saveTaskRequest.getHashtags());
        this.description = saveTaskRequest.getDescription();
        this.status = saveTaskRequest.getStatus();
    }
}
