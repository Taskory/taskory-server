package taskflower.taskflower.task;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SaveTaskRequset {

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String tag;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

}
