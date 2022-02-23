package ru.digitalhabbits.homework1.service;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);

    private static final String PLUGIN_EXT = "jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName) {
        // TODO: NotImplemented

        List<File> plugins = Arrays.asList(Objects.requireNonNull(new File(pluginDirName).listFiles()));

        URL[] urls = plugins
                .stream()
                .map(namePlugin -> buildUrl(pluginDirName, namePlugin.getName()))
                .toArray(URL[]::new);

        return loadJars(urls);
    }

    private synchronized List<Class<? extends PluginInterface>> loadJars(@Nonnull URL[] urls) {
        try {
            final URLClassLoader childClassLoader =
                    new URLClassLoader(urls, this.getClass().getClassLoader());

            return ClassPath.from(childClassLoader)
                    .getTopLevelClasses(PACKAGE_TO_SCAN)
                    .stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(cls -> PluginInterface.class.isAssignableFrom(cls) && !cls.isInterface())
                    .map(cls -> (Class<? extends PluginInterface>) cls)
                    .peek(cls -> logger.info("Loaded '{}' plugin", cls.getSimpleName()))
                    .collect(toList());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Nonnull
    private URL buildUrl(@Nonnull String dir, @Nonnull String fileName) {
        try {
            return new File(dir + "/" + fileName).toURI().toURL();
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
