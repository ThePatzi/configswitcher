package com.pichler.configswitcher.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppichler on 13.06.2016.
 */
public class Application {
  private static final Application INSTANCE = new Application();

  public static Application getInstance() {
    return INSTANCE;
  }

  private Map<String, String> parameters = new HashMap<>();
  private Configurations configurations;

  public Application() {
  }

  public void putParameter(String key, String value) {
    parameters.put(key, value);
  }

  public String getParameter(String key) {
    return parameters.get(key);
  }

  public String getParameter(String key, String defaultValue) {
    return parameters.getOrDefault(key, defaultValue);
  }

  public void setConfigurations(Configurations configurations) {
    this.configurations = configurations;
  }

  public Configurations getConfigurations() {
    return configurations;
  }
}
