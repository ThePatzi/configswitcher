package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.exceptions.EndExecutionException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Patrick on 19.05.2016.
 */
public class SimpleCopyTask extends AbstractTask {
    boolean overrideExisting = false;

    @Override
    public void setSettings(Map<String, String> settings) {
        super.setSettings(settings);

        overrideExisting = settings.containsKey("overrideExisting");
    }

    @Override
    public void execute() {
        String from = parameters.get("from");
        String to = parameters.get("to");

        if (from == null || to == null) {
            throw new EndExecutionException();
        }

        Path fromPath = Paths.get(from);
        Path toPath = Paths.get(to);

        try {
            if (overrideExisting) {
                Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(fromPath, toPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
