package com.example.backlogprojectv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
    private int resourceLayout;
    private Context mContext;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Task task = getItem(position);
        if(task != null){
            TextView nameTaskView = v.findViewById(R.id.nameTaskView);
            TextView poidTaskView = v.findViewById(R.id.poidTaskView);
            TextView personneAssigneTaskView = v.findViewById(R.id.personneAssigneTaskView);

            if(nameTaskView != null){
                nameTaskView.setText(task.getNom()+" - n°"+task.getId());
            }
            if(poidTaskView != null){
                poidTaskView.setText(String.valueOf(task.getPoid()));
            }
            if(personneAssigneTaskView != null){
                personneAssigneTaskView.setText(task.getPersonneAssigne());
            }
        }
        return  v;
    }
        public TaskArrayAdapter(Context context,int ressource, List<Task> tasks){
            super(context, ressource,tasks);
            this.resourceLayout = ressource;
            this.mContext = context;
        }
}
