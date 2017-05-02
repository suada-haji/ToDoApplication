package com.example.suadahaji.todoapplication.views;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suadahaji.todoapplication.utils.BaseApplication;
import com.example.suadahaji.todoapplication.R;
import com.example.suadahaji.todoapplication.database.TaskDbHelper;
import com.example.suadahaji.todoapplication.model.TaskModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<TaskModel> taskList;
    TaskAdapter adapter;
    private SQLiteDatabase database;
    private TaskDbHelper taskDbHelper;
    private int taskPosition;
    private ActionMode mActionMode;
    private double completedTasks;
    private  int doneTasks;
    private double totalTasks;

    @BindView(R.id.listTask)
    ListView listView;
    @BindView(R.id.etNewTask)
    EditText etNewItem;
    @BindView(R.id.btnAddTask)
    Button btnAddTask;
    @BindView(R.id.progress_header_one)
    TextView tvHeaderOne;
    @BindView(R.id.progress_header_two)
    TextView tvHeaderTwo;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_listview_header)
    TextView tvListViewHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        init();
    }

    public void init() {
        taskDbHelper = new TaskDbHelper(this);
        database = taskDbHelper.getWritableDatabase();
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(this);
        listTask(taskList);
        onLongClick(listView);
        getCompletedTasks();

        tvHeaderOne.setTypeface(BaseApplication.ROBOTO_MEDIUM);
        tvHeaderOne.setPaintFlags(tvHeaderOne.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvHeaderTwo.setTypeface(BaseApplication.ROBOTO_REGULAR);
        tvProgress.setTypeface(BaseApplication.ROBOTO_REGULAR);
        tvListViewHeader.setTypeface(BaseApplication.ROBOTO_BLACK);
        tvListViewHeader.setPaintFlags(tvListViewHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        etNewItem.setTypeface(BaseApplication.ROBOTO_MEDIUM);
        btnAddTask.setTypeface(BaseApplication.ROBOTO_BOLD);
        progressBar.setScaleY(3f);
        progressBar.setMax(100);
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
        EditText taskET = (EditText) parent.findViewById(R.id.etNewTask);
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
        getCompletedTasks();

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

    public void getCompletedTasks() {
        ArrayList<TaskModel> tasks = new ArrayList<>();
        for (TaskModel model : taskList) {
            if (model.isSelected()) {
                tasks.add(model);
                doneTasks = tasks.size();

            }
        }

        totalTasks = (double) taskList.size();
        completedTasks = Math.round(((doneTasks/totalTasks)*100) * 100D) / 100D;
        progressBar.setProgress((int) completedTasks);
        tvProgress.setText(completedTasks + getString(R.string.progress_percentage));
    }
}
