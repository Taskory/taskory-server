package codeartitect.taskflower.routine;

import codeartitect.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "days")
    private byte[] days = {0, 0, 0, 0, 0, 0, 0};

    public Routine(User user, SaveRoutineRequest saveRoutineRequest) {
        this.user = user;
        this.title = saveRoutineRequest.getTitle();
        this.description = saveRoutineRequest.getDescription();
        this.days = saveRoutineRequest.getDays();
    }

    public void update(SaveRoutineRequest saveRoutineRequest) {
        this.title = saveRoutineRequest.getTitle();
        this.description = saveRoutineRequest.getDescription();
        this.days = saveRoutineRequest.getDays();
    }
}
