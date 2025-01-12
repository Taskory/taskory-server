package codearchitect99.taskory.event;

import codearchitect99.taskory.common.BaseTimeEntity;
import codearchitect99.taskory.hashtag.Hashtag;
import codearchitect99.taskory.tag.model.Tag;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)          // If a user is deleted, the mapped events are also deleted
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag tag;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Hashtag> hashtags = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "due_date_time")
    private LocalDateTime dueDateTime;

    @Column(name = "location")
    private String location;
}
