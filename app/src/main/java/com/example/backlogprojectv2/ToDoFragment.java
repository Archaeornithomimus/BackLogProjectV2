package com.example.backlogprojectv2;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {
    DatabaseHandler db = null;
    TaskArrayAdapter taskArrayAdapter;
    ArrayList<Task> taskArray;
    ListView listView;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.todo_fragment, container, false);

        context = getActivity();
        db = new DatabaseHandler(context);

        listView = view.findViewById(R.id.listeTachesToDo);
        printList();

        //listView.setOnItemClickListener(this);
        return view;
    }

    public void printList(){
        taskArray = db.getAllToDoTask();
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
    }

    public void updateList(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
