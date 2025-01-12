package codearchitect99.taskory.tag.model;

import codearchitect99.taskory.tag.payload.SaveTagRequest;
import codearchitect99.taskory.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)          // If a user is deleted, the mapped tags are also deleted
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "color", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color color;

    public void update(SaveTagRequest saveTagRequest) {
        this.title = saveTagRequest.getTitle();
        this.color = Color.valueOf(saveTagRequest.getColor());
    }
}
