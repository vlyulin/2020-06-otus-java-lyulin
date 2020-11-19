package ru.otus.messagesystem.message;

public enum MessageType {

    VOID_TECHNICAL_MESSAGE("voidTechnicalMessage"),
    GET_USER("UserData"),
    GET_FILTERED_USERS("FilteredUsersData"),
    SAVE_USER("SaveUser"),
    DELETE_USER("DeleteUser"),
    OPERATION_STATUS("OperationStatus");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
