package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.config.Application;
import com.pichler.configswitcher.config.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * Created by ppichler on 16.06.2016.
 */
public class LoadPropertyFileTask extends AbstractTask {

  private boolean useApplication = false;

  @Override
  public void setSettings(Map<String, String> settings) {
    super.setSettings(settings);
    useApplication = Boolean.valueOf(settings.getOrDefault("useApplication", "false"));
  }

  @Override
  public void execute() {
    String path = getParameter("path");

    Properties properties = new Properties();
    try {
      properties.load(Files.newInputStream(Paths.get(path)));

      BiConsumer<String, String> attrSetter = useApplication ? Application.getInstance()::putParameter : Context.getCurrent()::putParameter;

      properties.keySet().forEach(k -> attrSetter.accept(k + "", properties.get(k) + ""));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
