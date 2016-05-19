package com.pichler.configswitcher.tasks;

import com.pichler.configswitcher.model.Task;

import java.util.Map;

/**
 * Created by Patrick on 19.05.2016.
 */
public abstract class AbstractTask implements Task {
    protected Map<String, String> parameters;
    protected Map<String, String> settings;

    @Override
    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String key){
        return parameters.get(key);
    }
}
