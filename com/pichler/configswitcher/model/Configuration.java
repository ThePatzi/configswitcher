package com.pichler.configswitcher.model;

import java.util.Collection;

/**
 * Created by Patrick on 18.05.2016.
 */
public class Configuration {
    public Collection<Task> tasks;
    public String name;

    public Configuration(Collection<Task> tasks, String name) {
        this.tasks = tasks;
        this.name = name;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
