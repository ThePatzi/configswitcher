package com.pichler.configswitcher.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrick on 18.05.2016.
 */
public class TaskSupplier {
    private Class<? extends Task> taskClass;
    private String name;
    private Map<String, String> settings;

    public TaskSupplier(Class<? extends Task> taskClass, String name, Map<String, String> settings) {
        this.taskClass = taskClass;
        this.name = name;
        this.settings = settings;
    }

    public TaskDraft getTask(Map<String, String> parameters, Map<String, String> overriddenSettings) {
        Map<String, String> settings = new HashMap<>(this.settings);
        settings.putAll(overriddenSettings);
        return new TaskDraft(taskClass, settings, parameters);
    }

    public Class<? extends Task> getTaskClass() {
        return taskClass;
    }

    public String getName() {
        return name;
    }
}
