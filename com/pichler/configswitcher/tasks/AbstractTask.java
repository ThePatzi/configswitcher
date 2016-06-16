package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.model.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Patrick on 19.05.2016.
 */
public abstract class AbstractTask implements Task {
  protected Map<String, Supplier<String>> parameters;
  protected Map<String, String> settings;

  private Supplier<String> emptySupplier = () -> null;

  @Override
  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }

  @Override
  public void setParameters(Map<String, Supplier<String>> parameters) {
    this.parameters = parameters;
  }

  public String getParameter(String key) {
    return parameters.getOrDefault(key, () -> null).get();
  }

  public String getParameter(String key, String defaultValue) {
    return parameters.getOrDefault(key, () -> defaultValue).get();
  }

  public String getSetting(String key) {
    return settings.get(key);
  }

  public String getSetting(String key, String defaultValue) {
    return settings.getOrDefault(key, defaultValue);
  }

  public Map<String, String> evaluateAllParameters() {
    Map<String, String> ret = new HashMap<>();

    parameters.replaceAll((key, supplier) -> {
      String value = supplier.get();
      ret.put(key, value);
      return () -> value;
    });

    return ret;
  }
}
