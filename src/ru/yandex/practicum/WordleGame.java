package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

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
public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private PrintWriter logger;

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        if (dictionary == null) {
            throw new RuntimeException("Словарь не может быть пустым");
        }
        if (logger == null) {
            throw new RuntimeException("Логгер не может быть пустым");
        }
        this.answer = dictionary.getRandomWord();
        this.steps = 0;
        this.dictionary = dictionary;
        this.logger = logger;

        if (this.answer == null) {
            throw new RuntimeException("Не получено слово из словаря");
        }
        logger.println("Игра создана, загадано: " + this.answer);
    }

    public boolean isRightWord(String word) {
        if (word == null) {
            throw new RuntimeException("Слово не может быть пустым");
        }
        return (answer.equals(word));
    }

    public String writeEncryptedAnswer(String word) {
        StringBuilder encryptedAnswer = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == word.charAt(i)) {
                encryptedAnswer.append("+");
            } else if (answer.charAt(i) != word.charAt(i) && answer.indexOf(word.charAt(i)) != -1) {
                encryptedAnswer.append("^");
            } else {
                encryptedAnswer.append("-");
            }
        }
        word = encryptedAnswer.toString();
        return word;
    }

    static class WordNotFoundInDictionary extends Exception {
        public WordNotFoundInDictionary(String message) {
            super(message);
        }
    }

    static class IllegalLengthOfWord extends Exception {
        public IllegalLengthOfWord(String message) {
            super(message);
        }
    }

    public boolean isWordCorrect(String word) throws WordNotFoundInDictionary, IllegalLengthOfWord {
        boolean first = false;
        boolean second = false;
        if (word.length() == answer.length()) {
            first = true;
        } else {
            String error = "Неправильная длина слова";
            logger.println(error);
            throw new IllegalLengthOfWord(error);
        }
        if (dictionary.containsWord(word)) {
            second = true;
        } else {
            String error = "Этого слова нет в словаре";
            logger.println(error);
            throw new WordNotFoundInDictionary(error);
        }
        return first && second;
    }

    public String correctingWord(String word) {
        word = word.toLowerCase();
        word = word.replace('ё', 'е');
        return word;
    }

    public String helpToPlayer(Map<String, String> letters) {
        for (String potentialWord : dictionary.getList()) {
            if (potentialWord.length() == 5 && !(letters.containsKey(potentialWord))) {
                for (Map.Entry<String, String> entry : letters.entrySet()) {
                    String usedWord = entry.getKey();
                    String encryptedUsedWord = entry.getValue();
                    if (isWordMatches(usedWord, potentialWord, encryptedUsedWord)) {
                        return potentialWord;
                    }

                }
            }
        }
        return "Нет подходящих слов";
    }

    private boolean isWordMatches(String usedWord, String potentialWord, String encryptedUsedWord) {
        if (!potentialWord.equals(usedWord)) {
            for (int i = 0; i < usedWord.length(); i++) {
                if (encryptedUsedWord.charAt(i) == '+') {
                    if (potentialWord.charAt(i) != usedWord.charAt(i)) {
                        return false;
                    }

                }

                if (encryptedUsedWord.charAt(i) == '^') {
                    if (potentialWord.charAt(i) == usedWord.charAt(i)) {
                        return false;
                    }
                    if (!(potentialWord.indexOf(usedWord.charAt(i)) != -1)) {
                        return false;
                    }
                }

                if (encryptedUsedWord.charAt(i) == '-') {
                    if (potentialWord.indexOf(usedWord.charAt(i)) != -1) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public void gameCreator() {
            Map<String, String> letters = new LinkedHashMap<>();

            Scanner scanner = new Scanner(System.in);
            int count = 0;
            while (true) {
                System.out.println("Введите слово");
                String word = scanner.nextLine();
                word = correctingWord(word);
                try {
                    if (word.isBlank()) {
                        logger.println("Игрок запросил подсказку компьютера");
                        String potentialWord = helpToPlayer(letters);
                        System.out.println("Возможно подойдёт " + potentialWord);
                    }
                    if (isWordCorrect(word)) {
                        if (isRightWord(word)) {
                            System.out.println("Это правильное слово!");
                            logger.println("Игрок угадал слово!");
                            break;
                        } else {
                            System.out.println(writeEncryptedAnswer(word));
                        }
                        steps++;
                        count++;
                        String encryptedAnswer = writeEncryptedAnswer(word);
                        letters.put(word, encryptedAnswer);
                        System.out.println("Количество попыток: " + count);
                        logger.println("Количество попыток игрока: " + count);
                    } else {
                        System.out.println("Введите другое слово");

                    }
                    if (steps == 6) {
                        System.out.println("Все попытки исчерпаны, загаданное слово: " + answer);
                        logger.println("Все попытки игрока исчерпаны, загаданное слово: " + answer);
                        break;
                    }
                } catch (WordNotFoundInDictionary e) {
                    logger.println(e.getMessage());
                    System.out.println("Этого слова нет в словаре");
                } catch (IllegalLengthOfWord e) {
                    logger.println(e.getMessage());
                    System.out.println("Неправильная длина слова");
                }
            }
            System.out.println(letters);
    }
}
