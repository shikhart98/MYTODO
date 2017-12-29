package com.example.shikh.mytodo.model;

/**
 * Created by shikh on 22-12-2017.
 */

public class task {

    String name;
    boolean isDone;

    public task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
