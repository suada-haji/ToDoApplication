package com.example.suadahaji.todoapplication.database;

/**
 * Created by suadahaji
 */

public class TaskModel  {

    private int task_id;

    private String task_name;

    private boolean selected = false;

    public TaskModel() {
    }

    public TaskModel(String task_name, boolean selected) {
        this.task_name = task_name;
        this.selected = selected;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
