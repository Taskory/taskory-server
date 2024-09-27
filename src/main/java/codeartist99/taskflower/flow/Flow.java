package codeartist99.taskflower.flow;

import codeartist99.taskflower.common.BaseTimeEntity;
import codeartist99.taskflower.flow.payload.SaveFlowRequest;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
        this.hashtags = saveFlowRequest.getHashtags();
        this.description = saveFlowRequest.getDescription();
    }


    public void update(SaveFlowRequest saveFlowRequest) {
        this.title = saveFlowRequest.getTitle();
        this.hashtags = saveFlowRequest.getHashtags();
        this.description = saveFlowRequest.getDescription();
    }
}
