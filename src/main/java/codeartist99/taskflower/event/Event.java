package codeartist99.taskflower.event;

import codeartist99.taskflower.common.BaseTimeEntity;
import codeartist99.taskflower.common.Timezone;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Hashtag> hashtags = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "due_date_time")
    private LocalDateTime dueDateTime;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)  // Use the Timezone enum as String in the database
    @Column(name = "timezone", nullable = false)
    private Timezone timezone;

    // Retrieve the ZoneId from the Timezone enum
    public ZoneId getEventZoneId() {
        return timezone.getZoneId();
    }

}
