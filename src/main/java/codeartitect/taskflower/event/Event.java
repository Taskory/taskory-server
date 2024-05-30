package codeartitect.taskflower.event;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.global.BaseTimeEntity;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtags = new HashSet<>();

    @Column
    private String description;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime dueDateTime;

    @Column
    private String location;

    public Event(User user, SaveEventRequest saveEventRequest) {
        this.user = user;
        this.title = saveEventRequest.getTitle();
        this.tag = saveEventRequest.getTag();
        setHashtags(saveEventRequest.getHashtags());
        this.description = saveEventRequest.getDescription();
        this.startDateTime = saveEventRequest.getStartDateTime();
        this.dueDateTime = saveEventRequest.getDueDateTime();
        this.location = saveEventRequest.getLocation();
    }

    private void setHashtags(Set<Hashtag> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) {
            this.hashtags = new HashSet<>();
        } else this.hashtags = hashtags;
    }

    @Override
    public ZoneId getUserZoneId() {
        return ZoneId.of(user.getZoneId());
    }

    public void update(SaveEventRequest saveEventRequest) {
        this.title = saveEventRequest.getTitle();
        this.tag = saveEventRequest.getTag();
        setHashtags(saveEventRequest.getHashtags());
        this.description = saveEventRequest.getDescription();
        this.startDateTime = saveEventRequest.getStartDateTime();
        this.dueDateTime = saveEventRequest.getDueDateTime();
        this.location = saveEventRequest.getLocation();
    }
}
