package com.pichler.configswitcher.tasks;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Created by Patrick on 19.05.2016.
 */
public class WriteTask extends AbstractTask {
    private OpenOption[] openOptions = null;

    @Override
    public void setSettings(Map<String, String> settings) {
        super.setSettings(settings);

        String openOptions = settings.getOrDefault("openOptions", "CREATE,APPEND");
        StringTokenizer tokenizer = new StringTokenizer(openOptions.trim(), ",");

        this.openOptions = Collections.list(tokenizer).stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(StandardOpenOption::valueOf)
                .filter(Objects::nonNull)
                .toArray(OpenOption[]::new);

    }

    @Override
    public void execute() {
        String file = getParameter("file");
        String content = getParameter("content", "");

        if (file == null || content == null) {
            return;
        }

        String processor = getParameter("processor");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(processor);
            Invocable invocable = (Invocable) engine;
            Object contentResult = invocable.invokeFunction(getSetting("processorFunction", "process"), content);
            content = contentResult + "";
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        Path filePath = Paths.get(file);
        try {
            Files.write(filePath, content.getBytes(), openOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
