package com.example.backlogprojectv2;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ActivityMembers extends ListActivity {
    private ArrayAdapter<TeamMember> monAdaptateur;
    private ArrayList<TeamMember> membersList;
    DatabaseHandler db = null;
    ListView listView;

    private Button buttonOpenDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        db = new DatabaseHandler(this);
        listView = findViewById(R.id.listMember);

        this.buttonOpenDialog = (Button) this.findViewById(R.id.addMembre);
        this.buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_showMessage(v);
            }
        });
    }

    public void btn_showMessage(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMembers.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_members, null);
        EditText fullName = (EditText) mView.findViewById(R.id.editTextPersonName);
        Button btn_cancel = (Button) mView.findViewById(R.id.cancel_button);
        Button btn_okay = (Button) mView.findViewById(R.id.validate_button);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                if (name != null && !name.isEmpty()) {
                    //monAdaptateur.add();
                    monAdaptateur.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public void printListMembers(){
        membersList = new ArrayList<>();
        this.monAdaptateur = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, membersList);
        super.setListAdapter(this.monAdaptateur);
        membersList = db.getAllMembers();
        //listView.setAdapter(memberArrayAdapter);
    }
}