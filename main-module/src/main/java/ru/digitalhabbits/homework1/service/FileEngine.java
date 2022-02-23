package ru.digitalhabbits.homework1.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

public class FileEngine {
    private static final Logger logger = getLogger(FileEngine.class);
    private static final String RESULT_FILE_PATTERN = "%s/%s/results-%s.txt";
    private static final String RESULT_DIR_PATTERN = "%s/%s";
    private static final String RESULT_DIR = "results";
    private static final String RESULT_EXT = "txt";

    @SneakyThrows
    public boolean writeToFile(@Nonnull String text, @Nonnull String searchString, @Nonnull String pluginName) {
        // TODO: NotImplemented

        String fileName = RESULT_DIR + "/" + searchString + "/results-" + pluginName + ".txt";

        Files.createDirectories(Path.of(RESULT_DIR + "/" + searchString));

        final File resultWork = new File(fileName);
        FileWriter writer = new FileWriter(resultWork);
        writer.write(text);
        writer.flush();

        return true;
    }

    public void cleanResultDir(@Nonnull String searchString) {
        final File resultDir = new File(RESULT_DIR + "/" + searchString);
        if (resultDir.exists()) {
            stream(resultDir.list((dir, name) -> name.endsWith(RESULT_EXT)))
                    .forEach(fileName -> new File(resultDir + "/" + fileName).delete());
        }
    }
}
