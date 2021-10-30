package com.example.backlogprojectv2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.todo_fragment, container, false);
        Task task1 = new Task("Lancement","ToDo","12/09","Bob",12,"toot");
        Task task2 = new Task("Dev","ToDo","31/09","Roger",2,"toot");
        ListView listView = view.findViewById(R.id.listeTachesToDo);

        ArrayList<Task> taches = new ArrayList<Task>();
        taches.add(task1);
        taches.add(task2);

        TaskArrayAdapter taskAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taches);
        listView.setAdapter(taskAdapter);
        //listView.setOnItemClickListener(this);


        return view;
    }

}
