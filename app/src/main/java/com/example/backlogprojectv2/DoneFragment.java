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

public class DoneFragment extends Fragment {
    DatabaseHandler db = null;
    TaskArrayAdapter taskArrayAdapter;
    ArrayList<Task> taskArray;
    ListView listView;
    private Context context;
    Spinner agilePrioritySpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.done_fragment, container, false);

        agilePrioritySpinner = (Spinner) view.findViewById(R.id.spinnerTri);
        ArrayList<String> prioriteAgileSpinnerList = new ArrayList<>();
        prioriteAgileSpinnerList.add("None");
        prioriteAgileSpinnerList.add("Could");
        prioriteAgileSpinnerList.add("Can");
        prioriteAgileSpinnerList.add("Should");

        ArrayAdapter<String> adapterAgilePriority= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,prioriteAgileSpinnerList);
        adapterAgilePriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agilePrioritySpinner.setAdapter(adapterAgilePriority);
        agilePrioritySpinner.setSelection(0);

        agilePrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                printListTask(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        context = getActivity();
        db = new DatabaseHandler(context);

        listView = view.findViewById(R.id.listeTachesDone);
        printListTask("None");
        //listView.setOnItemClickListener(this);
        return view;
    }

    public void printListTask(String filter){
        taskArray = db.getAllDoneTask(filter);
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), R.layout.tasks_list, taskArray);
        listView.setAdapter(taskArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskDoneDescription(view,task);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                showTaskDoneModificationDialog(view,task);
                return true;
            }
        });
    }

    public void showTaskDoneModificationDialog(View view, Task task){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_task_modif, null);

        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("ToDo");
        stateList.add("InProgress");
        stateList.add("Done");

        ArrayList<String> priorityList = new ArrayList<>();
        priorityList.add("Could");
        priorityList.add("Can");
        priorityList.add("Should");

        EditText name = (EditText) mView.findViewById(R.id.editTextTaskName);
        EditText poid = (EditText) mView.findViewById(R.id.editTextTaskPoid);
        EditText endDate = (EditText) mView.findViewById(R.id.editTextTaskEndDate);
        EditText description = (EditText) mView.findViewById(R.id.editTextDescription);

        Spinner state = (Spinner) mView.findViewById(R.id.spinnerTaskState);
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,stateList);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapterState);
        state.setSelection(2);

        Spinner priority = (Spinner) mView.findViewById(R.id.spinnerPriority);
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,priorityList);
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapterPriority);
        if(task.getPriority().equals("Could")){
            priority.setSelection(0);
        } else if (task.getPriority().equals("Can")){
            priority.setSelection(1);
        }
        else if (task.getPriority().equals("Should")){
            priority.setSelection(2);
        }

        Spinner personInCharge = (Spinner) mView.findViewById(R.id.spinnerPersonInCharge);
        ArrayList<TeamMember> teamMemberArrayList = db.getAllMembers();
        ArrayAdapter<TeamMember> adapterPersonInCharge = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,teamMemberArrayList);
        adapterPersonInCharge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personInCharge.setAdapter(adapterPersonInCharge);

        name.setText(task.getNom());
        poid.setText(Integer.toString(task.getPoid()));
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
                if(!name.getText().toString().isEmpty() && !poid.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){

                    String nameNew = name.getText().toString();
                    String endDateNew  = endDate.getText().toString();
                    int poidNew = Integer.parseInt(poid.getText().toString());
                    String stateNew = state.getSelectedItem().toString();
                    String descriptionNew = description.getText().toString();
                    TeamMember personInChargeNew = (TeamMember) personInCharge.getSelectedItem();
                    String priorityNew = (String)priority.getSelectedItem();
                    Task newTask = new Task(nameNew,poidNew,(TeamMember)personInChargeNew, stateNew,endDateNew,descriptionNew,priorityNew);
                    db.updateTask(task,newTask);
                    taskArrayAdapter.remove(task);
                    taskArrayAdapter.add(newTask);
                    taskArrayAdapter.notifyDataSetChanged();
                    agilePrioritySpinner.setSelection(0);
                    printListTask("None");
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

    public  void showTaskDoneDescription(View view,Task task) {
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
