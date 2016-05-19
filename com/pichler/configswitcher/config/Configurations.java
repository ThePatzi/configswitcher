package com.pichler.configswitcher.config;

import com.pichler.configswitcher.model.Configuration;
import com.pichler.configswitcher.model.TaskSupplier;

import java.util.Map;

/**
 * Created by Patrick on 18.05.2016.
 */
public class Configurations {
    private Map<String, TaskSupplier> taskSuppliers;
    private Map<String, Configuration> configurations;

    public Configurations(Map<String, TaskSupplier> taskSuppliers,
                          Map<String, Configuration> configurations) {
        this.taskSuppliers = taskSuppliers;
        this.configurations = configurations;
    }

    public Map<String, TaskSupplier> getTaskSuppliers() {
        return taskSuppliers;
    }

    public void setTaskSuppliers(Map<String, TaskSupplier> taskSuppliers) {
        this.taskSuppliers = taskSuppliers;
    }

    public Map<String, Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Configuration> configurations) {
        this.configurations = configurations;
    }
}
