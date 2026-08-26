package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionaryException extends GameException {
    public WordNotFoundInDictionaryException(String word) {
        super("Слово «" + word + "» не найдено в словаре");
    }
}