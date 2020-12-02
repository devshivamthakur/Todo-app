package com.example.todo;

public class todo_type {
    String work_name;
    boolean check;

    public todo_type(String work_name, boolean check) {
        this.work_name = work_name;
        this.check = check;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
