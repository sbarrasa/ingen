package com.accenture.powerbank.user.api;

import java.io.Serializable;

public record UserEvent(
    String action,
    UserDto user
) implements Serializable {
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_DELETE = "DELETE";
}
