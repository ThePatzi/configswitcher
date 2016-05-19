package com.pichler.configswitcher.model;

import java.util.Map;

/**
 * Created by Patrick on 18.05.2016.
 */
public class TaskDraft {
    private Map<String, String> parameters;
    private Map<String, String> settings;
    private Class<? extends Task> taskClass;

    public TaskDraft(Class<? extends Task> taskClass, Map<String, String> settings, Map<String, String> parameters) {
        this.parameters = parameters;
        this.settings = settings;
        this.taskClass = taskClass;
    }

    public Task getTask() {
        Task ret = getTaskInstance();
        ret.setParameters(parameters);
        ret.setSettings(settings);
        return ret;
    }

    private Task getTaskInstance() {
        try {
            return taskClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
