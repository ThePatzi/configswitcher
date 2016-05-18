package com.pichler.configswitcher.model;

import java.util.Map;

/**
 * Created by Patrick on 18.05.2016.
 */
public interface Task {
    void setParameters(Map<String, String> parameters);

    void execute();
}
