package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        try (PrintWriter logger = new PrintWriter(new FileWriter("game.log", false))) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            List<String> words = loader.readingFile("words_ru.txt");
            WordleDictionary dictionary = new WordleDictionary(words, logger);
            WordleGame game = new WordleGame(dictionary, logger);
            gameCreator(game, logger, dictionary);
            logger.println("Игра завершена");
        } catch (Throwable t) {
            System.out.println("Критическая ошибка: " + t.getMessage());
        }
    }

    public static void gameCreator(WordleGame game, PrintWriter logger, WordleDictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> letters = new LinkedHashMap<>();
        int steps = 0;
        int count = 0;

        while (true) {
            logger.println("Игра началась, загаданное слово: " + game.getAnswer());
            System.out.println("Введите слово");
            String word = scanner.nextLine();
            word = game.correctingWord(word);

            if (word.isBlank()) {
                logger.println("Игрок запросил подсказку компьютера");
                if (letters.isEmpty()) {
                    String potentialWord = dictionary.getRandomWord();
                    System.out.println("Возможно подойдёт " + potentialWord);
                } else {
                    String potentialWord = game.getHintFromCollections();
                    System.out.println("Возможно подойдёт " + potentialWord);
                }
                continue;
            }

            try {
                if (game.isWordCorrect(word)) {
                    if (game.isRightWord(word)) {
                        System.out.println("Это правильное слово!");
                        logger.println("Игрок угадал слово!");
                        break;
                    } else {
                        String encryptedAnswer = game.writeEncryptedAnswer(word);
                        System.out.println(encryptedAnswer);
                        game.sortUsedLettersForCollections(word, encryptedAnswer);
                    }
                    steps++;
                    count++;
                    String encryptedAnswer = game.writeEncryptedAnswer(word);
                    letters.put(word, encryptedAnswer);
                    System.out.println("Количество попыток: " + count);
                    logger.println("Количество попыток игрока: " + count);
                } else {
                    System.out.println("Введите другое слово");

                }
                if (steps == game.MAX_STEPS) {
                    System.out.println("Все попытки исчерпаны, загаданное слово: " + game.getAnswer());
                    logger.println("Все попытки игрока исчерпаны");
                    break;
                }
            } catch (WordleGame.WordNotFoundInDictionary e) {
                logger.println(e.getMessage());
                System.out.println("Этого слова нет в словаре");
            } catch (WordleGame.IllegalLengthOfWord e) {
                logger.println(e.getMessage());
                System.out.println("Неправильная длина слова");
            }
        }
        System.out.println(letters);
    }
}
