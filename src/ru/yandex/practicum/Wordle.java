package ru.yandex.practicum;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */

import ru.yandex.practicum.exceptions.GameException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        String logFileName = "wordle.log";
        String dictFileName = "words_ru.txt";

        try (PrintWriter log = new PrintWriter(new FileWriter(logFileName, StandardCharsets.UTF_8, true), true)) {
            log.println("=== Запуск новой сессии Wordle ===");

            WordleDictionary dictionary = WordleDictionaryLoader.load(dictFileName, log);
            WordleGame game = new WordleGame(dictionary, log);

            playGame(game, new Scanner(System.in));
        } catch (Exception e) {
            System.out.println("Произошла критическая ошибка приложения. Подробности записаны в лог: " + logFileName);
            e.printStackTrace();
        }
    }

    private static void playGame(WordleGame game, Scanner scanner) {
        System.out.println("Добро пожаловать в игру Wordle (5 букв)!");
        System.out.println("Обозначения: [+] — буква на месте, [^] — буква есть в слове, [-] — буквы нет.");
        System.out.println("Нажмите Enter на пустой строке, если хотите получить подсказку от компьютера.\n");

        while (!game.isGameOver()) {
            System.out.printf("Попытка (%d/%d). Введите слово: ",
                    (WordleGame.MAX_STEPS - game.getRemainingSteps() + 1),
                    WordleGame.MAX_STEPS);

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                String hint = game.getHint();
                System.out.println("Подсказка от компьютера: " + hint);
                continue;
            }

            try {
                String clue = game.makeGuess(input);
                System.out.println("> " + input.toLowerCase().replace('ё', 'е'));
                System.out.println("> " + clue);
                System.out.println();
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте еще раз.\n");
            }
        }

        System.out.println("----------------------------------------");
        if (game.isWon()) {
            System.out.println("Поздравляем! Вы отгадали слово!");
        } else {
            System.out.println("Ходы закончились! Вы проиграли.");
        }
        System.out.println("Загаданное слово было: " + game.getAnswer().toUpperCase());
    }
}