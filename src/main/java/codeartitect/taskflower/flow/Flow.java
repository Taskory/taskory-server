package codeartitect.taskflower.flow;

import codeartitect.taskflower.flow.payload.SaveFlowRequest;
import codeartitect.taskflower.common.BaseTimeEntity;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Flow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
public class Flow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Hashtag> hashtags = new ArrayList<>();

    @Column(name = "description")
    private String description;

    public Flow(User user, SaveFlowRequest saveFlowRequest) {
        this.user = user;
        this.title = saveFlowRequest.getTitle();
        setHashtags(saveFlowRequest.getHashtags());
        this.description = saveFlowRequest.getDescription();
    }

    private void setHashtags(Set<Hashtag> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) {
            this.hashtags = new ArrayList<>();
        }
    }

    @Override
    public ZoneId getUserZoneId() {
        return ZoneId.of(this.user.getZoneId());
    }

    public void update(SaveFlowRequest saveFlowRequest) {
        this.title = saveFlowRequest.getTitle();
        setHashtags(saveFlowRequest.getHashtags());
        this.description = saveFlowRequest.getDescription();
    }
}
