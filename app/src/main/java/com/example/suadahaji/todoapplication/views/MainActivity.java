package com.example.suadahaji.todoapplication.views;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suadahaji.todoapplication.R;
import com.example.suadahaji.todoapplication.database.TaskDbHelper;
import com.example.suadahaji.todoapplication.model.TaskModel;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    ArrayList<TaskModel> taskList;
    TaskAdapter adapter;
    private SQLiteDatabase database;
    private TaskDbHelper taskDbHelper;
    private int taskPosition;
    private ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDbHelper = new TaskDbHelper(this);
        database = taskDbHelper.getWritableDatabase();
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);

        listView = (ListView) findViewById(R.id.listTask);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(this);
        listTask(taskList);
        onLongClick(listView);
    }

    /**
     * List all tasks in the Database
     */

    public void listTask(ArrayList<TaskModel> tasksList) {
        QueryResultIterable<TaskModel> taskQuery = null;
        database = taskDbHelper.getReadableDatabase();

        // Get the cursor for this query
        Cursor tasks = cupboard().withDatabase(database).query(TaskModel.class).getCursor();
        try {
            // Iterate Tasks
            taskQuery = cupboard().withCursor(tasks).iterate(TaskModel.class);
            for (TaskModel task : taskQuery) {
                tasksList.add(0, task);
            }
        } finally {
            // close the cursor
            tasks.close();
        }

    }

    /**
     * Create a new Task
     */

    public void createTask(View view) {

        TaskModel model = new TaskModel();
        View parent = (View) view.getParent();
        EditText taskET = (EditText) parent.findViewById(R.id.etNewItem);
        String task_title = taskET.getText().toString().trim();
        if (task_title.matches("")) {
            return;
        } else {
            model.setTask_name(task_title);
        }

        cupboard().withDatabase(database).put(model);
        taskET.setText("");
        reload();

    }

    /**
     * Mark an item as complete
     */

    public void updateTask(View view) {

        View parent = (View) view.getParent();
        CheckBox selected = (CheckBox) parent.findViewById(R.id.is_selected);
        TextView name = (TextView) parent.findViewById(R.id.task_title);

        TaskModel model = taskList.get(taskPosition);

        model.setTask_name(String.valueOf(name.getText()));
        model.setSelected(selected.isChecked());
       // Log.d("Bool", "Position is " + taskPosition);
        ContentValues values = new ContentValues(1);
        values.put("selected", model.isSelected());
        cupboard().withDatabase(database).update(TaskModel.class, values, "task_name = ?", model.getTask_name());

        reload();
    }

    /**
     * Reload the listview
     */

    private void reload(){
        taskList.clear();
        listTask(taskList);
        adapter.notifyDataSetChanged();

    }

    private void onLongClick(ListView listView) {
        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }
                taskPosition = position;
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });
    }

    /**
     * Delete a task
     */

    public void deleteTask(ArrayList<TaskModel> models, int position) {
        TaskModel taskModel = models.get(position);
        cupboard().withDatabase(database).delete(TaskModel.class, taskModel.get_id());
        models.remove(position);
        reload();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        taskPosition = position;
    }

    //CONTEXTUAL ACTION CALLBACK
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        /**
         * Called when the action mode is created; startActionMode() was called
         */

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Options");
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

        /**
         * Called each time the action mode is shown.
         */

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }


        /**
         * Called when the user selects a contextual menu item
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    deleteTask(taskList, taskPosition);
                    Toast.makeText(MainActivity.this, "Task moved to trash", Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Called when the user exits the action mode
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}
