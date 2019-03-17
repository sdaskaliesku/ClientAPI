package com.client.domain.enums;

/**
 * @author sdaskaliesku
 */
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"), //0
    ROLE_USER("ROLE_USER"), // 1
    ROLE_NOTHING("ROLE_NOTHING"), // 2
    ROLE_READONLY("ROLE_READONLY"); // 2

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getString() {
        return super.toString().replace("ROLE_", "");
    }
}
