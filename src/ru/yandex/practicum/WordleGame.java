package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

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

    public static final int MAX_STEPS = 6;
    public static final int MAX_LENGTH = 5;
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private PrintWriter logger;
    private Set<Character> usableLetters;
    private Set<Character> unusableLetters;
    private Map<Integer, Character> rightLetters;

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
        this.usableLetters = new HashSet<>();
        this.unusableLetters = new HashSet<>();
        this.rightLetters = new LinkedHashMap<>();

        if (this.answer == null) {
            throw new RuntimeException("Не получено слово из словаря");
        }
        logger.println("Игра создана, загадано: " + this.answer);
    }

    public String getAnswer() {
        return answer;
    }

    public void sortUsedLettersForCollections(String word, String result) {
        for (int i = 0; i < answer.length(); i++) {
            char wordChar = word.charAt(i);
            char resultChar = result.charAt(i);
            if (resultChar == '+') {
                usableLetters.add(wordChar);
                rightLetters.put(i, wordChar);
            } else if (resultChar == '^') {
                usableLetters.add(wordChar);
            } else if (resultChar == '-') {
                if (!usableLetters.contains(wordChar)) {
                    unusableLetters.add(wordChar);
                }
            }
        }
    }

    public String getHintFromCollections() {
        for (String potentialWord : dictionary.getList()) {
            if (isWordMatches(potentialWord)) {
                return potentialWord;
            }
        }
        return "Нет подходящих слов";
    }

    private boolean isWordMatches(String potentialWord) {
        for (Map.Entry<Integer, Character> entry : rightLetters.entrySet()) {
            int numberOfLetter = entry.getKey();
            int letter = entry.getValue();
            if (potentialWord.charAt(numberOfLetter) != letter) {
                return false;
            }
        }

        for (char letter : usableLetters) {
            if (potentialWord.indexOf(letter) == -1) {
                return false;
            }
        }

        for (char letter : unusableLetters) {
            if (potentialWord.indexOf(letter) != -1) {
                return false;
            }
        }

        return true;
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
        word = word.trim().toLowerCase();
        word = word.replace('ё', 'е');
        return word;
    }
}
