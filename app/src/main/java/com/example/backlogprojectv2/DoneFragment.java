package com.example.backlogprojectv2;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class DoneFragment extends Fragment {
    private DatabaseHandler db = null;
    TaskArrayAdapter taskArrayAdapter;
    ArrayList<Task> taskArray = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.done_fragment, container, false);
        ListView listView = view.findViewById(R.id.listeTachesDone);
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
        //listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.db = new DatabaseHandler(getActivity());
        taskArray = db.getAllInProgressTask();
        for (Task t:taskArray) {
            taskArrayAdapter.add(t);
        }
        taskArrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
