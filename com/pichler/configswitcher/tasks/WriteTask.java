package com.pichler.configswitcher.tasks;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Patrick on 19.05.2016.
 */
public class WriteTask extends AbstractTask {
    @Override
    public void execute() {
        String file = getParameter("file");
        String content = getParameter("content");

        if (file == null || content == null) {
            return;
        }

        String processor = getParameter("processor");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(processor);
            Invocable invocable = (Invocable) engine;
            Object contentResult = invocable.invokeFunction("process", content);
            content = contentResult + "";
            System.out.println(content);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        Path filePath = Paths.get(file);

    }
}
