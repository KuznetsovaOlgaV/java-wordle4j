package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleDictionary dictionary;
    private PrintWriter log;

    @BeforeEach
    void setUp() {
        List<String> words = List.of("герой", "гонец", "поезд", "шапка", "папка", "ручка", "ребро");
        dictionary = new WordleDictionary(words);
        log = new PrintWriter(System.out);
    }

    @Test
    void testCalculateClueExactAndPartialMatches() {
        String clue = WordleGame.calculateClue("герой", "гонец");
        assertEquals("+^-^-", clue);
    }

    @Test
    void testCalculateClueDuplicateLetters() {
        String clue = WordleGame.calculateClue("шапка", "папка");
        assertEquals("-++++", clue);
    }

    @Test
    void testMakeGuessSuccessWin() throws GameException {
        WordleGame game = new WordleGame(dictionary, "герой", log);
        String clue = game.makeGuess("герой");
        assertEquals("+++++", clue);
        assertTrue(game.isWon());
        assertTrue(game.isGameOver());
    }

    @Test
    void testInvalidLengthException() {
        WordleGame game = new WordleGame(dictionary, "герой", log);
        assertThrows(InvalidWordLengthException.class, () -> game.makeGuess("кот"));
    }

    @Test
    void testWordNotFoundException() {
        WordleGame game = new WordleGame(dictionary, "герой", log);
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeGuess("абвгд"));
    }

    @Test
    void testEmptyDictionaryException() {
        WordleDictionary emptyDict = new WordleDictionary(Collections.emptyList());
        assertThrows(EmptyDictionaryException.class, emptyDict::getRandomWord);
    }

    @Test
    void testDictionaryLoaderFileNotFound() {
        assertThrows(DictionaryLoadException.class, () -> WordleDictionaryLoader.load("non_existent_file.txt", log));
    }

    @Test
    void testHintGeneration() throws GameException {
        WordleGame game = new WordleGame(dictionary, "папка", log);
        game.makeGuess("шапка");
        String hint = game.getHint();
        assertEquals("папка", hint);
    }

    @Test
    void testAttemptsExhausted() throws GameException {
        WordleGame game = new WordleGame(dictionary, "герой", log);
        for (int i = 0; i < 6; i++) {
            assertFalse(game.isGameOver());
            game.makeGuess("поезд");
        }
        assertTrue(game.isGameOver());
        assertFalse(game.isWon());
    }
}