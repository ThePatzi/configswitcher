package com.pichler.configswitcher.tasks;

/**
 * Created by Patrick on 19.05.2016.
 */
public class TestTask extends AbstractTask {
  @Override
  public void execute() {
    evaluateAllParameters();

    System.out.println(parameters);
    System.out.println(settings);
  }
}
