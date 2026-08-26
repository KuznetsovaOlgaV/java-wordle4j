package ru.yandex.practicum.exceptions;

public class InvalidWordLengthException extends GameException {
    public InvalidWordLengthException(int length) {
        super("Слово должно состоять ровно из 5 букв (введено: " + length + ")!");
    }
}