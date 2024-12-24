package storagebox.entities.security;

public enum Role {
    ROLE_MANAGER("Manager"),
    ROLE_ADMIN("Admin"),
    ROLE_USER("User");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
