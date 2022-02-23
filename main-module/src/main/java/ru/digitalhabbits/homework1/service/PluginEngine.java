package ru.digitalhabbits.homework1.service;

import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PluginEngine {

    @Nonnull
    public <T extends PluginInterface> String applyPlugin(@Nonnull Class<T> cls, @Nonnull String text) {
        // TODO: NotImplemented

        try {
            PluginInterface plugin = cls.newInstance();
            return Objects.requireNonNull(plugin.apply(text));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return "";
    }
}
