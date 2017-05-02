package com.example.suadahaji.todoapplication.model;

/**
 * Created by suadahaji
 */

public class TaskModel  {

    private Long _id;

    private String task_name;

    private boolean selected;

    public TaskModel() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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
