package com.pichler.configswitcher.transformers;

import com.pichler.configswitcher.model.Transformer;

import java.util.Map;

/**
 * Created by ppichler on 16.06.2016.
 */
public abstract class AbstractTransformer implements Transformer {
  protected String name;
  protected Map<String, String> settings;

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void initialize(Map<String, String> settings) {
    this.settings = settings;
  }
}
