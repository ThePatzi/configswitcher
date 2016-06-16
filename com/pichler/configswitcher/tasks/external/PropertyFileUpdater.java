package com.pichler.configswitcher.tasks.external;

import com.pichler.configswitcher.tasks.AbstractTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by ppichler on 13.06.2016.
 */
public class PropertyFileUpdater extends AbstractTask {
  @Override
  public void execute() {
    String key = getParameter("key");
    String value = getParameter("value");

    if (key == null) {
      System.out.println("The 'key'-parameter has not been found!");
      return;
    }

    String targetString = getParameter("target");

    if (targetString == null) {
      System.out.println("The 'target'-parameter has not been found!");
      return;
    }

    Predicate<String> pattern = Pattern.compile("^" + key + "=.*").asPredicate();

    for (String targetPath : targetString.split(";")) {
      Path target = parameters.containsKey("target") ? Paths.get(targetPath) : null;

      if (!Files.exists(target)) {
        try {
          Files.createDirectories(target.getParent());
          Files.createFile(target);
        } catch (IOException e) {
          e.printStackTrace();
          return;
        }
      }

      try {
        List<String> lines = Files.readAllLines(target);
        List<String> newLines = new ArrayList<>();

        boolean added = false;

        for (String line : lines) {
          if (pattern.test(line)) {
            added = true;
            newLines.add(key + "=" + value);
            continue;
          }

          newLines.add(line);
        }

        if (!added) {
          newLines.add(key + "=" + value);
        }

        Files.write(target, newLines, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
