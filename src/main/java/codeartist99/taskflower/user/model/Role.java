package codeartist99.taskflower.user.model;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    TEMP_USER("ROLE_TEMPORARY_USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
