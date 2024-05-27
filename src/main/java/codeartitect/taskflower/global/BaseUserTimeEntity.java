package codeartitect.taskflower.global;

import codeartitect.taskflower.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
public abstract class BaseUserTimeEntity {

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @ManyToOne(optional = false)
    private User user;

    @PrePersist
    public void prePersist() {
        if (user != null) {
            ZoneId zoneId = ZoneId.of(user.getZoneId());
            this.created_at = LocalDateTime.now(zoneId);
            this.updated_at = LocalDateTime.now(zoneId);
        } else throw new IllegalStateException("User Not Found");
    }

    @PreUpdate
    public void preUpdate() {
        if (user != null) {
            ZoneId zoneId = ZoneId.of(user.getZoneId());
            this.updated_at = LocalDateTime.now(zoneId);
        } else throw new IllegalStateException("User Not Found");
    }
}
