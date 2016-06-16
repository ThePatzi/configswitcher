package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.config.Context;

import java.util.Map;

/**
 * Created by ppichler on 16.06.2016.
 */
public class ContextTask extends AbstractTask {
  private boolean startNewContext = false;
  private boolean clearContext = false;

  @Override
  public void setSettings(Map<String, String> settings) {
    super.setSettings(settings);
    startNewContext = Boolean.valueOf(settings.getOrDefault("new", "false"));
    startNewContext = Boolean.valueOf(settings.getOrDefault("clear", "false"));
  }

  @Override
  public void execute() {
    Context context = startNewContext ? Context.startNewContext() : Context.getCurrent();

    if(clearContext && !startNewContext){
      context.clear();
    }

    parameters.forEach(context::putParameter);
  }
}
