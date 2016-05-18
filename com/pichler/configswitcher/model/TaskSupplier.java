package com.pichler.configswitcher.model;

import java.util.Map;

/**
 * Created by Patrick on 18.05.2016.
 */
public class TaskSupplier {
    private Class<? extends Task> taskClass;
    private String name;

    public TaskSupplier(String name, Class<? extends Task> taskClass) {
        this.name = name;
        this.taskClass = taskClass;
    }

    public Task getTask(Map<String, String> parameters) {
        Task ret = getTaskInstance();

        if (ret != null) {
            ret.setParameters(parameters);
        }

        return ret;
    }

    private Task getTaskInstance() {
        try {
            return taskClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<? extends Task> getTaskClass() {
        return taskClass;
    }

    public String getName() {
        return name;
    }
}
