package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    private PrintWriter logger;

    public WordleDictionary(List<String> words, PrintWriter logger) {
        this.words = words;
        this.logger = logger;
    }

    public List<String> getList() {
        return words;
    }

    public static int getSizeOfList(List<String> words) {
        return words.size();
    }

    public String getRandomWord() {
        Random rnd = new Random();
        int index = rnd.nextInt(getSizeOfList(words));
        while (words.get(index).length() != 5) {
            index = rnd.nextInt(getSizeOfList(words));
        }
        return words.get(index);
    }

    public boolean containsWord(String word) {
        return words.contains(word);
    }

}
