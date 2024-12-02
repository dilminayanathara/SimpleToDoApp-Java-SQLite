package com.example.todolist.Adapter;

import static com.example.todolist.Util_package.Util.DATABASE_TABLE;
import static com.example.todolist.Util_package.Util.KEY_DESCRIPTION;

import static com.example.todolist.Util_package.Util.KEY_ID;
import static com.example.todolist.Util_package.Util.KEY_STATUS;
import static com.example.todolist.Util_package.Util.KEY_TITLE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Util_package.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY, "
                + Util.KEY_TITLE + " TEXT, "
                + KEY_DESCRIPTION + " TEXT, "
                + Util.KEY_STATUS + " INTEGER"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                DATABASE_TABLE);
        onCreate(sqLiteDatabase);

    }

    public SQLiteDatabase db;

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel toDoModel) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_STATUS, 0);
        values.put(Util.KEY_TITLE, toDoModel.getTitle());
        values.put(KEY_DESCRIPTION, toDoModel.getDes());

        db.insert(DATABASE_TABLE, null, values);
db.close();
    }

    @SuppressLint({"Range", "Recycle"})
    public List<ToDoModel> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Start the transaction
            db.beginTransaction();

            // Query the database
            cursor = db.query(Util.DATABASE_TABLE, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(cursor.getColumnIndex(Util.KEY_ID)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    task.setDes(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }

            // Mark the transaction as successful
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure cursor is closed and transaction ends
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }

        return taskList;
    }


    public void updateStates(int id, int status) {
        ContentValues values = new ContentValues();
        values.put(Util.KEY_STATUS, status);
        db.update(Util.DATABASE_TABLE, values, Util.KEY_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    public void updateTask(int id, String title, String des) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, title); // Make sure this is the correct constant name
        values.put(Util.KEY_DESCRIPTION, des); // Use the correct constant for description

        // Update the database row based on the provided ID
        db.update(Util.DATABASE_TABLE, values, Util.KEY_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id){
        Log.d("DatabaseHandler", "Task ID: " + id + " has been deleted/updated");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.DATABASE_TABLE, Util.KEY_ID + "=?" , new String[] {
                String.valueOf(id)});
        db.close();

    }


}




