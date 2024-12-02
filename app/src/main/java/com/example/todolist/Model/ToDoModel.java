package com.example.todolist.Model;



//Define the Individual task


public class ToDoModel {



    int id,status;
    String title,des;

    public ToDoModel() {
    }
    public ToDoModel(int id, int status, String title, String des) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.des = des;
    }

    public ToDoModel(int status, String title, String des) {
        this.status = status;
        this.title = title;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
