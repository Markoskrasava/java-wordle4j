package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        PrintWriter logger = null;
        try {
            logger = new PrintWriter(new FileWriter("game.log", false));
            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            List<String> words = loader.readingFile("words_ru.txt");
            WordleDictionary dictionary = new WordleDictionary(words, logger);
            WordleGame game = new WordleGame(dictionary, logger);
            game.gameCreator();
        } catch (Throwable t) {
            logger.println(t.getMessage());
        } finally {
            logger.close();
        }
    }
}
