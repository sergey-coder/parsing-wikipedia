package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented

        final String patternForWord =  "(\\b[a-zA-Z][a-zA-Z.\\-0-9]*\\b)";
        final Matcher matcher = Pattern.compile(patternForWord).matcher(text);

        final Stream.Builder<String> streamBuilder = Stream.builder();

        while (matcher.find()){
            streamBuilder.add(matcher.group().toLowerCase(Locale.ROOT));
        }
        Map<String, Long> result = streamBuilder.build()
                .collect(Collectors.groupingBy((it -> it), Collectors.counting()));
        Map<String, Long> map = new TreeMap<>(result);

        final StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String,Long> element: map.entrySet()){
            stringBuilder.append(element.getKey()).append(" ").append(element.getValue()).append("\n");
        }

        return stringBuilder.toString();
    }
}
