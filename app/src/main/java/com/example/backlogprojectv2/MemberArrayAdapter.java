package com.example.backlogprojectv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MemberArrayAdapter extends ArrayAdapter<TeamMember> {
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

        TeamMember teamMember = getItem(position);
        if(teamMember != null){
            TextView nameTaskView = v.findViewById(R.id.name_team_member_view);
            TextView fistNameView = v.findViewById(R.id.first_name_team_member_view);

            if(nameTaskView != null){
                nameTaskView.setText(teamMember.getName());
            }
            if(fistNameView != null){
                fistNameView.setText(teamMember.getFirstname());
            }
        }
        return  v;
    }

    public MemberArrayAdapter(Context context, int ressource, List<TeamMember> TeamMember){
        super(context, ressource,TeamMember);
        this.resourceLayout = ressource;
        this.mContext = context;
    }
}
