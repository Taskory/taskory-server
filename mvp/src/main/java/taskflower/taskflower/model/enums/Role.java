package taskflower.taskflower.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    TEMP_USER("ROLE_TEMPORARY_USER"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String description;
}

