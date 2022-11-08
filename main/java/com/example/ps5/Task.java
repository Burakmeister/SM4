package com.example.ps5;

import java.util.Date;
import java.util.UUID;

public class Task{
    private Category category;
    private final UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task(String name, boolean isDone){
        id = UUID.randomUUID();
        date = new Date();
        this.name = name;
        this.done = isDone;
    }
    public Task(){
        this.id = UUID.randomUUID();
        this.date = new Date();
        this.name = "";
        this.done = false;
        this.category = Category.HOME;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory(){
        return this.category;
    }

    public void setCategory(Category category){
        this.category=category;
    }

    public void setDate(Date date){
        this.date = date;
    }
}