package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final List<String> words;
    private final Set<String> wordsSet;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
        this.wordsSet = new HashSet<>(words);
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        return wordsSet.contains(normalize(word));
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст. Невозможно выбрать случайное слово.");
        }
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public int size() {
        return words.size();
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace('ё', 'е');
    }
}