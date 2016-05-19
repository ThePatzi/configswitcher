package com.pichler.configswitcher.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Patrick on 19.05.2016.
 */
public class SimpleDeleteTask extends AbstractTask {
    @Override
    public void execute() {
        String pathStr = getParameter("path");

        if (pathStr == null) {
            return;
        }

        Path path = Paths.get(pathStr);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
