package codeartist99.taskflower.routine.model;

import codeartist99.taskflower.routine.payload.SaveRoutineRequest;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)          // If a user is deleted, the mapped routines are also deleted
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "days", nullable = false)
    private boolean[] days = {false, false, false, false, false, false, false};

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
