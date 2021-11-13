package com.example.backlogprojectv2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
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
            TextView dateDeFinTaskView = v.findViewById(R.id.dateDeFinTaskView);
            TextView etatTaskView = v.findViewById(R.id.etatTaskView);


            if(nameTaskView != null){
                nameTaskView.setText(task.getNom()+" - n°"+task.getId());
            }
            if(poidTaskView != null){
                poidTaskView.setText(String.valueOf(R.string.priorityNumberLabel +task.getPoid()));
            }
            if(personneAssigneTaskView != null){
                if (task.getPersonneAssigne() != "None") {
                    personneAssigneTaskView.setText(R.string.inChargeLabel + task.getPersonneAssigne());

                    // OU
                    //personneAssigneTaskView.setText(task.getPersonneAssigne() + R.string.inChargeLabel2);
                } else{
                    personneAssigneTaskView.setText(R.string.nobodyInChargeLabel);
                }
            }
            if(etatTaskView != null){
                etatTaskView.setText(task.getEtat());
            }
            if(dateDeFinTaskView != null){
                dateDeFinTaskView.setText(R.string.endDateLabel + task.getDateDeFin());

                Calendar calendar = Calendar.getInstance();
                if(calendar.after(task.getDateDeFin())){
                    dateDeFinTaskView.setTextColor(Color.RED);
                }
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
