package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented

        long countLine = text.split("(\\n)").length;

        Pattern patternWords = Pattern.compile("(\\b[a-zA-Z][a-zA-Z.\\-0-9]*\\b)");
        Matcher matcherWords = patternWords.matcher(text);

        List<String> words = new ArrayList<>();

        while (matcherWords.find()) {
            words.add(matcherWords.group());
        }

        long countWord = words.size();
        long countSymbol = words.stream().map(String::length).mapToInt(Integer::intValue).sum();

        return String.format("%d;%d;%d", countLine, countWord, countSymbol);
    }


}
