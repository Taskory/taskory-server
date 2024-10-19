package codeartist99.taskflower.tag.model;

import codeartist99.taskflower.tag.payload.SaveTagRequest;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "color", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color color;

    public void update(SaveTagRequest saveTagRequest) {
        this.title = saveTagRequest.getTitle();
        this.color = Color.valueOf(saveTagRequest.getColor());
    }
}
