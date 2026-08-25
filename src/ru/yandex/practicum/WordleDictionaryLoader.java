package ru.yandex.practicum;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public static WordleDictionary load(String filename, PrintWriter log) throws IOException {
        log.println("Загрузка словаря из файла: " + filename);
        List<String> fiveLetterWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = WordleDictionary.normalize(line);
                  if (normalized.length() == 5 && normalized.matches("[а-я]+")) {
                    fiveLetterWords.add(normalized);
                }
            }
        } catch (IOException e) {
            log.println("Ошибка при чтении файла словаря: " + e.getMessage());
            throw e;
        }

        if (fiveLetterWords.isEmpty()) {
            throw new IllegalStateException("В файле " + filename + " не найдено подходящих 5-буквенных слов.");
        }

        log.println("Словарь успешно загружен. Доступно слов для игры: " + fiveLetterWords.size());
        return new WordleDictionary(fiveLetterWords);
    }
}