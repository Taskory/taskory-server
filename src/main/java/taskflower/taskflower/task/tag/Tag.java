package taskflower.taskflower.task.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import taskflower.taskflower.user.User;

@Entity
@Data
public class Tag {
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
