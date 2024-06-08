package codeartitect.taskflower.Tag.model;

import codeartitect.taskflower.Tag.payload.SaveTagRequest;
import codeartitect.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Tag(User user, SaveTagRequest saveTagRequest) {
        this.user = user;
        this.title = saveTagRequest.getTile();
        this.color = saveTagRequest.getColor();
    }

    public void update(SaveTagRequest saveTagRequest) {
        this.title = saveTagRequest.getTile();
        this.color = saveTagRequest.getColor();
    }
}
