package com.example.todolist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;



import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private final MainActivity activity;
    private List<ToDoModel> toDoModelList;
    private Context context;
    private DatabaseHandler db;


    public ToDoAdapter(MainActivity activity, Context context, List<ToDoModel> toDoModelList, DatabaseHandler db) {
        this.context = context;
        this.toDoModelList = toDoModelList;
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        return new ToDoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = toDoModelList.get(position); // from the position
        holder.titleid.setText(item.getTitle());
        holder.desid.setText(item.getDes());

        holder.task.setChecked(toBoolean(item.getStatus()));

        holder.task.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                db.updateStates(item.getId(), 1);
            } else {
                db.updateStates(item.getId(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<ToDoModel> toDoModelList) {
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    // Method to delete item from RecyclerView and DB
    public void deleteItem(int position) {
        ToDoModel item = toDoModelList.get(position);
        db.deleteTask(item.getId());
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    // Method to edit item from RecyclerView (to show dialog or edit page)
    @SuppressLint("WrongViewCast")
    public void editItem(int position,ToDoAdapter adapter) {
        // Get the task to be edited
        ToDoModel task = toDoModelList.get(position);

        // Create a new AlertDialog.Builder without setting a title
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate edit_task.xml
        View view = LayoutInflater.from(context).inflate(R.layout.edit_task, null);
        builder.setView(view); // Set the custom view for the dialog

        // Find EditText and other views in edit_task.xml
        EditText editTextTask = view.findViewById(R.id.edittitleid);
        EditText editTextdes = view.findViewById(R.id.editdesid);

        // Pre-fill with existing task text
        editTextTask.setText(task.getTitle());
        editTextdes.setText(task.getDes());

        // Configure dialog buttons
        Button saveButton = view.findViewById(R.id.editbutton);
        Button cancelButton = view.findViewById(R.id.cancelbutton);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            // Update the task and notify the adapter
            String newTitle = editTextTask.getText().toString();
            String newDescription = editTextdes.getText().toString();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                // Update the task object
                task.setTitle(newTitle);
                task.setDes(newDescription);

                // Save updated task to the database
                db.updateTask(task.getId(), task.getTitle(), task.getDes());

                // Update the list with the new task data
                toDoModelList.set(position, task);

                // Notify the adapter that the item has been updated
                adapter.notifyItemChanged(position);

                // Dismiss the dialog
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter both title and description", Toast.LENGTH_SHORT).show();
            }

        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss(); // Close dialog without saving
            notifyItemChanged(position);
        });

        // Create and show the dialog without a title
//        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleid, desid;
        CheckBox task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleid = itemView.findViewById(R.id.titleid);
            desid = itemView.findViewById(R.id.desid);
            task = itemView.findViewById(R.id.todocheckbox);
        }
    }
}
