package ru.yandex.practicum;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    public static final int MAX_STEPS = 6;
    public static final int WORD_LENGTH = 5;
    public static final int RUSSIAN_ALPHABET_SIZE = 33;

    private final String answer;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private int steps = 0;
    private boolean won = false;
    private final List<String> enteredWords = new ArrayList<>();
    private final List<String> cluesHistory = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this(dictionary, dictionary.getRandomWord(), log);
    }

    public WordleGame(WordleDictionary dictionary, String secretWord, PrintWriter log) {
        this.dictionary = Objects.requireNonNull(dictionary);
        this.answer = WordleDictionary.normalize(secretWord);
        this.log = log != null ? log : new PrintWriter(System.out);
        this.log.println("Новая игра создана. Загаданное слово: " + this.answer);
    }

    public String makeGuess(String rawGuess) throws GameException {
        if (isGameOver()) {
            throw new IllegalStateException("Игра уже завершена!");
        }

        String guess = WordleDictionary.normalize(rawGuess);

        if (guess.length() != WORD_LENGTH) {
            throw new InvalidWordLengthException(guess.length());
        }

        if (!guess.matches("[а-я]+")) {
            throw new InvalidCharactersException();
        }

        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionaryException(guess);
        }

        steps++;
        String clue = calculateClue(this.answer, guess);
        enteredWords.add(guess);
        cluesHistory.add(clue);

        log.println("Ход #" + steps + ": слово = " + guess + ", подсказка = " + clue);

        if (guess.equals(answer)) {
            won = true;
            log.println("Игрок угадал слово!");
        }

        return clue;
    }

    public static String calculateClue(String secret, String guess) {
        char[] result = new char[WORD_LENGTH];
        int[] letterCounts = new int[RUSSIAN_ALPHABET_SIZE];

        for (int i = 0; i < WORD_LENGTH; i++) {
            char s = secret.charAt(i);
            char g = guess.charAt(i);
            if (s == g) {
                result[i] = '+';
            } else {
                letterCounts[s - 'а']++;
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == '+') {
                continue;
            }
            char g = guess.charAt(i);
            int charIndex = g - 'а';
            if (charIndex >= 0 && charIndex < letterCounts.length && letterCounts[charIndex] > 0) {
                result[i] = '^';
                letterCounts[charIndex]--;
            } else {
                result[i] = '-';
            }
        }

        return new String(result);
    }

    public String getHint() {
        log.println("Запрошена подсказка от компьютера.");

        for (String candidate : dictionary.getWords()) {
            if (enteredWords.contains(candidate)) {
                continue;
            }

            boolean matchesAllRules = true;
            for (int i = 0; i < enteredWords.size(); i++) {
                String prevGuess = enteredWords.get(i);
                String expectedClue = cluesHistory.get(i);

                if (!calculateClue(candidate, prevGuess).equals(expectedClue)) {
                    matchesAllRules = false;
                    break;
                }
            }

            if (matchesAllRules) {
                log.println("Подсказка подобрана: " + candidate);
                return candidate;
            }
        }

        return dictionary.getRandomWord();
    }

    public boolean isGameOver() {
        return won || steps >= MAX_STEPS;
    }

    public boolean isWon() {
        return won;
    }

    public int getRemainingSteps() {
        return MAX_STEPS - steps;
    }

    public String getAnswer() {
        return answer;
    }
}