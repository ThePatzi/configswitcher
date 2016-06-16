package com.pichler.configswitcher;

import com.pichler.configswitcher.config.Application;
import com.pichler.configswitcher.config.Configurations;
import com.pichler.configswitcher.model.Configuration;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * Created by Patrick on 18.05.2016.
 */
public class Main {
  public static void main(String[] args) throws IOException, SAXException {
    if (args.length < 2) {
      System.out.println("Usage: [config.xml] [config-name]");
      return;
    }

    String configFilePath = args[0];
    String configName = args[1];

    parseParameters(args);

    Path configFile = Paths.get(configFilePath);

    if (!configFile.toFile().exists()) {
      System.out.println("File " + configFile.toAbsolutePath() + " does not exist!");
      return;
    }

    Configurations configurations = XMLParser.getInstance().loadConfigurations(Files.newInputStream(configFile));
    Application.getInstance().setConfigurations(configurations);

    Configuration config = configurations.getConfigurations().get(configName);

    if (config == null) {
      System.out.println("No config with the name: " + configName);
      return;
    }

    config.run();
  }

  private static void parseParameters(String[] args) {
    Application app = Application.getInstance();

    for (int i = 2; i < args.length; i++) {
      StringTokenizer stringTokenizer = new StringTokenizer(args[i], ";");
      while (stringTokenizer.hasMoreTokens()) {
        String toCheck = stringTokenizer.nextToken();

        String[] parts = toCheck.split("=", 2);

        if (parts.length == 1) {
          app.putParameter(parts[0], "");
        }

        if (parts.length == 2) {
          app.putParameter(parts[0], parts[1]);
        }
      }

    }
  }
}
