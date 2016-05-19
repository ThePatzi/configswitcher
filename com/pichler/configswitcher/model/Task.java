package com.pichler.configswitcher.model;

import java.util.Map;

/**
 * Created by Patrick on 19.05.2016.
 */
public interface Task {
    void setParameters(Map<String, String> parameters);

    void setSettings(Map<String, String> settings);

    void execute();
}
