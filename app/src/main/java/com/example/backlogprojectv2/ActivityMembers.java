package com.example.backlogprojectv2;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;



public class ActivityMembers extends ListActivity {

    private final String[] desserts = new String[]{
            "creme brulee", "crumble aux framboises",
            "panna cotta", "tarte aux pommes"
    };

    private ArrayAdapter<String> monAdaptateur;
    private ArrayList<String> listeDesserts;

    private Button buttonOpenDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        listeDesserts = new ArrayList<String>();

        for (int i = 0; i < desserts.length; i++)
            listeDesserts.add(desserts[i]);

        this.monAdaptateur = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listeDesserts);

        super.setListAdapter(this.monAdaptateur);

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
                if (name != null || name.isEmpty() != true) {
                    monAdaptateur.add(name);
                    monAdaptateur.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
}
