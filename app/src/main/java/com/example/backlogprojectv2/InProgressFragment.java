package com.example.backlogprojectv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class InProgressFragment extends Fragment {
    private DatabaseHandler db = null;
    TaskArrayAdapter taskArrayAdapter;
    ArrayList<Task> taskArray;
    ListView listView;
    private Context context;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.inprogress_fragment, container, false);
        context = getActivity();
        db = new DatabaseHandler(context);
        listView = view.findViewById(R.id.listeTachesInProgress);
        printListTask();
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
        //listView.setOnItemClickListener(this);
        return view;
    }

    public void printListTask(){
        taskArray = db.getAllInProgressTask();
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskInProgressDescription(view,task);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskInProgressModificationDialog(view,task);
                return true;
            }
        });
    }

    public void showTaskInProgressModificationDialog(View view, Task task){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_task_modif, null);

        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("ToDo");
        stateList.add("InProgress");
        stateList.add("Done");

        EditText name = (EditText) mView.findViewById(R.id.editTextTaskName);
        EditText priority = (EditText) mView.findViewById(R.id.editTextTaskPriority);
        EditText endDate = (EditText) mView.findViewById(R.id.editTextTaskEndDate);
        EditText description = (EditText) mView.findViewById(R.id.editTextDescription);

        Spinner state = (Spinner) mView.findViewById(R.id.spinnerTaskState);
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,stateList);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapterState);
        state.setSelection(1);

        Spinner personInCharge = (Spinner) mView.findViewById(R.id.spinnerPersonInCharge);
        ArrayList<TeamMember> teamMemberArrayList = db.getAllMembers();
        ArrayAdapter<TeamMember> adapterPersonInCharge = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,teamMemberArrayList);
        adapterPersonInCharge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personInCharge.setAdapter(adapterPersonInCharge);

        name.setText(task.getNom());
        priority.setText(Integer.toString(task.getPoid()));
        endDate.setText(task.getDateDeFin());
        description.setText(task.getDescription());
        int index = 0;

        if (task.getPersonneAssigne()!=null) {
            for (TeamMember teamMember:teamMemberArrayList
            ) {
                if (teamMember.getId()== task.getPersonneAssigne().getId()){
                    index = teamMemberArrayList.indexOf(teamMember);
                }
            }
        }
        personInCharge.setSelection(index);

        Button btnValidate = (Button) mView.findViewById(R.id.validateTasklModifButton);
        Button btnCancel = (Button) mView.findViewById(R.id.cancelTaskModifButton);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !priority.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){

                    String nameNew = name.getText().toString();
                    String endDateNew  = endDate.getText().toString();
                    int poidNew = Integer.parseInt(priority.getText().toString());
                    String stateNew = state.getSelectedItem().toString();
                    String descriptionNew = description.getText().toString();
                    TeamMember personInChargeNew = (TeamMember) personInCharge.getSelectedItem();
                    Task newTask = new Task(nameNew,poidNew,(TeamMember)personInChargeNew, stateNew,endDateNew,descriptionNew);
                    db.updateTask(task,newTask);
                    taskArrayAdapter.remove(task);
                    taskArrayAdapter.add(newTask);
                    taskArrayAdapter.notifyDataSetChanged();
                    printListTask();
                    alertDialog.dismiss();
                } else{
                    Toast.makeText(context,"Il faut remplir les champs",Toast.LENGTH_SHORT);
                }
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

    public  void showTaskInProgressDescription(View view,Task task) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_task_description, null);
        TextView description = (TextView) mView.findViewById(R.id.textViewTaskDescription1);
        TextView etat = (TextView) mView.findViewById(R.id.textViewState);
        FloatingActionButton deleteButton = mView.findViewById(R.id.floatingActionButtonDeleteTask);
        deleteButton.setActivated(true);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteTask(task);
                taskArray.remove(task);
                taskArrayAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
