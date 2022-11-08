package com.example.ps5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public final static String KEY_EXTRA_TASK_ID = "str";
    private final String CUR_SUBTITLE_VISIBLE = "idk";
    private boolean subtitleVisible=false;

    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.updateView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(CUR_SUBTITLE_VISIBLE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle(){
        TaskStorage ts = TaskStorage.getInstance();
        List<Task> tasks = ts.getTasks();
        int todoTasksCount = 0;
        for(Task task: tasks){
            if(!task.isDone()){
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format, todoTasksCount);
        if(!subtitleVisible){
            subtitle=null;
        }
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateView(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if(adapter == null){
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CUR_SUBTITLE_VISIBLE, subtitleVisible);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Task task;

        private final CheckBox checkBox;
        private final TextView nameTextView;
        private final TextView dateTextView;
        private final ImageView icon;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            checkBox = itemView.findViewById(R.id.task_completed);
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            icon = itemView.findViewById(R.id.icon_image);
        }

        public void blind(Task task){
            this.task = task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
            if(task.getCategory().equals(Category.HOME)){
                icon.setImageResource(R.drawable.ic_house);
            }else{
                icon.setImageResource(R.drawable.ic_studies);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

        public CheckBox getCheckBox(){
            return this.checkBox;
        }
    }

        private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
            private final List<Task> tasks;

            public TaskAdapter(List<Task> tasks){
                this.tasks = tasks;
            }

            @NonNull
            @Override
            public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new TaskHolder(layoutInflater, parent);
            }

            @Override
            public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
                Task task = tasks.get(position);
                CheckBox checkBox = holder.getCheckBox();
                checkBox.setChecked(tasks.get(position).isDone());
                checkBox.setOnCheckedChangeListener((buttonView, isChecked)->
                        tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked));
                holder.blind(task);
            }

            @Override
            public int getItemCount() {
                return tasks.size();
            }
        }
}
