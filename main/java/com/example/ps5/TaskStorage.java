package com.example.ps5;

import java.util.ArrayList;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private final ArrayList<Task> tasks;

    private TaskStorage(){
        this.tasks = new ArrayList<>();
        for(int i=1; i<150; i++){
            this.tasks.add(new Task("Pilne zadanie nr" + i, i % 2 == 0));
            if(i%3==0){
                tasks.get(i-1).setCategory(Category.STUDIES);
            }else{
                tasks.get(i-1).setCategory(Category.HOME);
            }
        }
    }

    public static TaskStorage getInstance(){
        return taskStorage;
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public Task getTask(UUID id){
        for(Task task: tasks){
            if(task.getId().compareTo(id) == 0){
                return task;
            }
        }
        return null;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}
