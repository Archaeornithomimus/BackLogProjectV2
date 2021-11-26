package com.example.backlogprojectv2;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

            if(nameTaskView != null){
                nameTaskView.setText(task.getNom()+" - tâche n°"+task.getId());
            }
            if(poidTaskView != null){
                poidTaskView.setText(getContext().getString(R.string.poidNumberLabel) +task.getPoid());
            }
            if(personneAssigneTaskView != null){
                if (task.getPersonneAssigne() != null) {
                    personneAssigneTaskView.setText(task.getPersonneAssigne().getNameMember() + " " +  getContext().getString(R.string.inChargeLabel2));
                } else{
                    personneAssigneTaskView.setText(getContext().getString(R.string.nobodyInChargeLabel));
                }
            }
            if(dateDeFinTaskView != null){
                dateDeFinTaskView.setText(getContext().getString(R.string.endDateLabel) +" "+ task.getDateDeFin());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    Date date = format.parse(task.getDateDeFin());
                    Date today = Calendar.getInstance().getTime();
                    if(today.after(date)){
                        dateDeFinTaskView.setTextColor(Color.argb(255,255,0,0));
                    } else {
                        dateDeFinTaskView.setTextColor(Color.argb(255,0,175,0));
                    }
                } catch (ParseException e){
                    Log.d("Parse Error",e.getMessage());
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
