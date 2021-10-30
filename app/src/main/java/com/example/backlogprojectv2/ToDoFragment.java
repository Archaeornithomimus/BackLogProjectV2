package com.example.backlogprojectv2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.todo_fragment, container, false);

        ListView listView = view.findViewById(R.id.listeTachesToDo);

        ArrayList<String> taches = new ArrayList<String>();
        taches.add("lancement");
        taches.add("dev");
        taches.add("remise client");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,taches);
        listView.setAdapter(arrayAdapter);
        //listView.setOnItemClickListener(this);


        return view;
    }

}
