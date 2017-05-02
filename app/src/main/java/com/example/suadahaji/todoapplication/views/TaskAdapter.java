package com.example.suadahaji.todoapplication.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.suadahaji.todoapplication.BaseApplication;
import com.example.suadahaji.todoapplication.R;
import com.example.suadahaji.todoapplication.model.TaskModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suadahaji
 */

public class TaskAdapter extends ArrayAdapter<TaskModel> {

    private Context context;

    private ArrayList<TaskModel> tasks;

    public TaskAdapter(Context context, ArrayList<TaskModel> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.tasks.addAll(tasks);
    }

    static class ViewHolder {
        @BindView(R.id.task_title)
        TextView task_name;
        @BindView(R.id.is_selected)
        CheckBox task_isSelected;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        ViewHolder viewHolder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row, parent, false);

            viewHolder = new ViewHolder(row);

            viewHolder.task_name = (TextView) row.findViewById(R.id.task_title);
            viewHolder.task_isSelected = (CheckBox) row.findViewById(R.id.is_selected);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        TaskModel taskModel = tasks.get(position);

        viewHolder.task_name.setText(taskModel.getTask_name());

        if (taskModel.isSelected()) {
            viewHolder.task_isSelected.setChecked(true);
            viewHolder.task_name.setPaintFlags(viewHolder.task_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.task_name.setTypeface(BaseApplication.ROBOTO_REGULAR);
        } else {
            viewHolder.task_isSelected.setChecked(false);
            viewHolder.task_name.setPaintFlags(viewHolder.task_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            viewHolder.task_name.setTypeface(BaseApplication.ROBOTO_MEDIUM);
        }
        return row;
    }
}
