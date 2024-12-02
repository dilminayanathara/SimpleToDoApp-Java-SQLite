package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.DatabaseHandler;
import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner {

    private RecyclerView recyclerView;
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> tasKList;
    private DatabaseHandler db;
    public FloatingActionButton fab;
    public ItemTouchHelper itemTouchHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize task list and adapter
        db = new DatabaseHandler(this);
        db.openDatabase();

        tasKList = new ArrayList<>();

        recyclerView = findViewById(R.id.taskRecyclerView);
        fab = findViewById(R.id.fab);

        // Set up RecyclerView and LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        taskAdapter = new ToDoAdapter(this, this, tasKList, db);

        // Attach adapter to RecyclerView
        recyclerView.setAdapter(taskAdapter);

        // Initialize the ItemTouchHelper and attach to RecyclerView
        itemTouchHelper = new ItemTouchHelper(new Recycleritemtouchhelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Get tasks from the database and update the list
        tasKList = db.getAllTasks();
        Collections.reverse(tasKList); // Reverse to show the latest tasks at the top
        taskAdapter.setTasks(tasKList);
        taskAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
    }

    // Method to handle dialog close and update task list

    public void handleDialogClose(DialogInterface dialog) {
        tasKList = db.getAllTasks();
        Collections.reverse(tasKList);
        taskAdapter.setTasks(tasKList);
        taskAdapter.notifyDataSetChanged();
    }

    // Method to show add task dialog
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_task, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText editTextTitle = dialogView.findViewById(R.id.titleid);
        EditText editTextDescription = dialogView.findViewById(R.id.desid);
        Button buttonSubmit = dialogView.findViewById(R.id.taskbutton);

        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty()) {
                ToDoModel task = new ToDoModel();
                task.setTitle(title);
                task.setDes(description);
                task.setStatus(0); // Default status

                db.insertTask(task);  // Insert new task to the database

                tasKList = db.getAllTasks();
                Collections.reverse(tasKList);
                taskAdapter.setTasks(tasKList);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    // Method to edit a task

}




