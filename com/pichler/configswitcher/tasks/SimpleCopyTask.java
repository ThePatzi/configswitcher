package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.exceptions.EndExecutionException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Created by Patrick on 19.05.2016.
 */
public class SimpleCopyTask extends AbstractTask {
    private CopyOption[] copyOptions;
    private boolean useAsIs = false;

    @Override
    public void setSettings(Map<String, String> settings) {
        super.setSettings(settings);

        this.useAsIs = Boolean.valueOf(settings.getOrDefault("useAsIs", "true"));

        String copyOptions = settings.getOrDefault("openOptions", "");
        StringTokenizer tokenizer = new StringTokenizer(copyOptions.trim(), ",");

        this.copyOptions = Collections.list(tokenizer).stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(StandardCopyOption::valueOf)
                .filter(Objects::nonNull)
                .toArray(CopyOption[]::new);
    }

    @Override
    public void execute() {
        String from = getParameter("from");
        String to = getParameter("to");

        if (from == null || to == null) {
            throw new EndExecutionException();
        }

        Path fromPath = Paths.get(from);
        Path toPath = Paths.get(to);
        Path toFilePath;

        if (useAsIs) {
            toFilePath = toPath;
        } else {
            toFilePath = Paths.get(toPath.toString(), fromPath.toFile().getName());
        }

        toFilePath.toFile().mkdirs();

        try {
            Files.copy(fromPath, toFilePath, copyOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
