package com.example.backlogprojectv2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class DoneFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.done_fragment, container, false);
        Task task1 = new Task("Apéro","Done","09/08","Bob",20,"toot");
        Task task2 = new Task("Apéro","done","15/07","Marc",10,"toot");
        ListView listView = view.findViewById(R.id.listeTachesDone);

        ArrayList<Task> taches = new ArrayList<Task>();
        taches.add(task1);
        taches.add(task2);

        TaskArrayAdapter taskAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taches);
        listView.setAdapter(taskAdapter);
        //listView.setOnItemClickListener(this);


        return view;
    }
}
