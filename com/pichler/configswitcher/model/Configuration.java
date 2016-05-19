package com.pichler.configswitcher.model;

import com.pichler.configswitcher.exceptions.EndExecutionException;

import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Created by Patrick on 18.05.2016.
 */
public class Configuration implements Runnable {
    public Collection<TaskDraft> taskDrafts;
    public String name;

    public Configuration(Collection<TaskDraft> taskDrafts, String name) {
        this.taskDrafts = taskDrafts;
        this.name = name;
    }

    public Collection<TaskDraft> getTaskDrafts() {
        return taskDrafts;
    }

    public void setTaskDrafts(Collection<TaskDraft> taskDrafts) {
        this.taskDrafts = taskDrafts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (TaskDraft taskDraft : taskDrafts) {
            Task task = taskDraft.getTask();

            try {
                task.execute();
            } catch (EndExecutionException ex) {
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
