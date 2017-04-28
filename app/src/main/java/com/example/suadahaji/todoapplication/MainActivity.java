package com.example.suadahaji.todoapplication;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.suadahaji.todoapplication.database.DbHelper;
import com.example.suadahaji.todoapplication.database.TaskAdapter;
import com.example.suadahaji.todoapplication.database.TaskModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView listView;
    ArrayList<TaskModel> taskList;
    TaskAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        listView = (ListView) findViewById(R.id.listTask);
        loadTaskList();


    }

    private void loadTaskList() {
        taskList = dbHelper.getTrackList();

        if (adapter == null) {
            adapter = new TaskAdapter(this, taskList);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void createTask(View view) {
        View parent = (View) view.getParent();
        EditText taskET = (EditText) parent.findViewById(R.id.etNewItem);
        String task = String.valueOf(taskET.getText());
        TaskModel model = new TaskModel();
        model.setTask_name(task);
        dbHelper.insertNewTask(model);
        taskET.setText("");
        loadTaskList();

    }

    /*public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTV = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTV.getText());
        TaskModel model = taskList.get()
        dbHelper.deleteTask(task);
        loadTaskList();
    }*/
}
