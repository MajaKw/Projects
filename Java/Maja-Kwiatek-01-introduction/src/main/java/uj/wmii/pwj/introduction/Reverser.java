package uj.wmii.pwj.introduction;

import java.util.*;

public class Reverser {

    public String reverse(String input) {
        if(input == null) return null;
        return new StringBuilder(input.trim()).reverse().toString();
    }

    public String reverseWords(String input) {
        if(input == null) return null;

        List<String> words = new ArrayList<>(Arrays.asList(input.split(" ")));
        words.replaceAll(String::trim);
        Collections.reverse(words);

        return String.join(" ",words);
    }

}
