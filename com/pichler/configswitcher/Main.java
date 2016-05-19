package com.pichler.configswitcher;

import com.pichler.configswitcher.config.Configurations;
import com.pichler.configswitcher.model.Configuration;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        Path configFile = Paths.get(configFilePath);

        if (!configFile.toFile().exists()) {
            System.out.println("File " + configFile.toAbsolutePath() + " does not exist!");
            return;
        }

        Configurations configurations = XMLParser.getInstance().loadConfigurations(Files.newInputStream(configFile));

        Configuration config = configurations.getConfigurations().get(configName);

        if (config == null) {
            System.out.println("No config with the name: " + configName);
            return;
        }

        config.run();
    }
}
