package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleGame game;
    private PrintWriter logger;
    private WordleDictionary dictionary;

    @BeforeEach
    void startForEveryTest() throws Exception {
        logger = new PrintWriter(new FileWriter("game.log", false));
        WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
        List<String> words = loader.readingFile("words_ru.txt");
        dictionary = new WordleDictionary(words, logger);
        game = new WordleGame(dictionary, logger);
        //  Wordle.gameCreator(game, logger, dictionary);
    }

    @Test
    void checkEveryWordInDictionaryFiveLetters() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        List<String> words = loader.readingFile("words_ru.txt");
        assertTrue(!words.isEmpty());
        for (String word : words) {
            assertEquals(5, word.length());
        }

    }

    @Test
    void allWordsLowerCase() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        List<String> words = loader.readingFile("words_ru.txt");
        for (String word : words) {
            assertEquals(word, word.toLowerCase());
        }
    }

    @Test
    void testIfPlayerGuessWord() {
        String answer = game.getAnswer();
        assertTrue(game.isRightWord(answer));
    }

    @Test
    void testGetHintMethod() {
        String hint = game.getHintFromCollections();
        assertTrue(!hint.isEmpty());
        assertEquals(hint, hint.toLowerCase());
        assertEquals(5, hint.length());
    }

    @Test
    void testHintMethodAfterWrongGuess() {
        String word = "банан";
        String encrypted = game.writeEncryptedAnswer(word);
        game.sortUsedLettersForCollections(word, encrypted);
        String hint = game.getHintFromCollections();
        assertTrue(!hint.isEmpty());
        assertNotEquals(word, hint);
    }

    @Test
    void testGameCreatorWithNoRightAnswer() {
        Map<String, String> letters = new LinkedHashMap<>();
        int count = 0;

            String[] words = {"банан", "конец", "гонец", "аванс", "ветер", "книга"};
            for (int i = 0; i < game.MAX_STEPS; i++) {
                String word = words[i];
                word = game.correctingWord(word);
                try {
                    if (game.isWordCorrect(word)) {
                        assertFalse(game.isRightWord(word));
                        if (game.isRightWord(word)) {
                            System.out.println("Это правильное слово!");
                            logger.println("Игрок угадал слово!");
                            break;
                        } else {
                            String encryptedAnswer = game.writeEncryptedAnswer(word);
                            System.out.println(encryptedAnswer);
                            game.sortUsedLettersForCollections(word, encryptedAnswer);
                            letters.put(word, encryptedAnswer);
                        }
                        count++;
                        System.out.println("Количество попыток: " + count);
                        logger.println("Количество попыток игрока: " + count);
                    }
                } catch (WordleGame.WordNotFoundInDictionary e) {
                    System.out.println("Слово не найдено в словаре");
                } catch (WordleGame.IllegalLengthOfWord e) {
                    System.out.println("Неправильная длина слова");
                }
            }
        assertEquals(count, game.MAX_STEPS);
        System.out.println(letters);
    }

    @Test
    void testGameCreatorWithRightAnswer() {
        Map<String, String> letters = new LinkedHashMap<>();
        int count = 0;

        String[] words = {"банан", "конец", "гонец", "аванс", "ветер", "книга"};
        for (int i = 0; i < game.MAX_STEPS; i++) {
            String word = words[i];
            if (i == 3) {
                word = game.getAnswer();
            }
            word = game.correctingWord(word);
            try {
                if (game.isWordCorrect(word)) {
                    if (game.isRightWord(word)) {
                        assertTrue(game.isRightWord(word));
                        System.out.println("Это правильное слово!");
                        logger.println("Игрок угадал слово!");
                        break;
                    } else {
                        String encryptedAnswer = game.writeEncryptedAnswer(word);
                        System.out.println(encryptedAnswer);
                        game.sortUsedLettersForCollections(word, encryptedAnswer);
                        letters.put(word, encryptedAnswer);
                    }
                    count++;
                    System.out.println("Количество попыток: " + count);
                    logger.println("Количество попыток игрока: " + count);
                }
            } catch (WordleGame.WordNotFoundInDictionary e) {
                System.out.println("Слово не найдено в словаре");
            } catch (WordleGame.IllegalLengthOfWord e) {
                System.out.println("Неправильная длина слова");
            }
        }
        assertTrue(count < game.MAX_STEPS);
        System.out.println(letters);
    }
}


