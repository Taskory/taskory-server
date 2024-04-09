package taskflower.taskflower.task.tag.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import taskflower.taskflower.global.BaseTimeEntity;
import taskflower.taskflower.user.model.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private User user;
}
