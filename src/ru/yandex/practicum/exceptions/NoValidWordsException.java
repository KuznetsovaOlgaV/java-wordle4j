package ru.yandex.practicum.exceptions;

public class NoValidWordsException extends RuntimeException {
    public NoValidWordsException(String message) {
        super(message);
    }
}