package com.example.backlogprojectv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

        listView = (ListView)view.findViewById(R.id.listeTachesToDo);
        printListTask();

        return view;
    }

    public void printListTask(){
        taskArray = db.getAllToDoTask();
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Task listItem = listView.getItemAtPosition(position);
                //TextView v = (Task) view.findViewById(R.id.txtLstItem);
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskToDoDescription(view,task);
                Toast.makeText(context, "selected Item Name is " + task.getId(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskToDoModificationDialog(view,task);
                return true;
            }
        });
    }

    public void showTaskToDoModificationDialog(View view, Task task){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_task_modif, null);



        EditText name = (EditText) mView.findViewById(R.id.editTextTaskName);
        EditText priority = (EditText) mView.findViewById(R.id.editTextTaskPriority);
        EditText endDate = (EditText) mView.findViewById(R.id.editTextTaskEndDate);
        EditText description = (EditText) mView.findViewById(R.id.editTextDescription);

        Spinner etat = (Spinner) mView.findViewById(R.id.spinnerTaskState);
        Spinner personInCharge = (Spinner) mView.findViewById(R.id.spinnerPersonInCharge);

        name.setText(task.getNom());
        priority.setText(Integer.toString(task.getPoid()));
        endDate.setText(task.getDateDeFin());
        etat.setPrompt(task.getEtat());
        personInCharge.setPrompt(task.getPersonneAssigne());

        description.setText(task.getDescription());
        Button btnValidate = (Button) mView.findViewById(R.id.validateTasklModifButton);
        Button btnCancel = (Button) mView.findViewById(R.id.cancelTaskModifButton);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"modification svg",Toast.LENGTH_LONG);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public  void showTaskToDoDescription(View view,Task task) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_task_description, null);
        TextView description = (TextView) mView.findViewById(R.id.textViewTaskDescription1);
        TextView etat = (TextView) mView.findViewById(R.id.textViewState);
        etat.setText(task.getEtat());
        description.setText(task.getDescription());
        Button btn_cancel = (Button) mView.findViewById(R.id.cancel_button);
        alert.setView(mView);
        alert.setTitle(task.getNom());
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /*public void showTaskToDo(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
