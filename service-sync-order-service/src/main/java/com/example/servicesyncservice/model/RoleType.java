package com.example.servicesyncservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_MECHANIC("ROLE_MECHANIC");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

