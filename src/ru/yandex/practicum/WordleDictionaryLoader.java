package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private static PrintWriter logger;

    public WordleDictionaryLoader(PrintWriter logger) {
        this.logger = logger;
    }

    public WordleDictionaryLoader() {

    }

    public static List<String> readingFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String word = reader.readLine();
                if (word.length() == 5) {
                    word = word.replace('ё', 'е').toLowerCase();
                    words.add(word);
                }
            }
        } catch (Exception e) {
            logger.println("Ошибка! " + e.getMessage());
            e.printStackTrace(logger);
        }
        return words;
    }
}
