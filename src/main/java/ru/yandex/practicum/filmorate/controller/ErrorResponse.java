package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

public class ErrorResponse {
    @Getter
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
