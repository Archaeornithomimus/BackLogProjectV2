package com.example.backlogprojectv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context,"backlogprojectv2",null,9);
        this.db = getWritableDatabase();
    }

    //region Administration de la base de donnée
    @Override
    public void onCreate(SQLiteDatabase db){
        this.db = db;
        db.execSQL("CREATE TABLE TaskToDo (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskDone (id INTEGER PRIMARY KEY UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskInProgress (id INTEGER PRIMARY KEY UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TeamMembers (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, prenom TEXT, nom TEXT);");


        // donnée test
        TeamMember alice = new TeamMember("tamere","Alice");
        TeamMember bob = new TeamMember("tamere","Bob");
        insertNewTeamMember(alice);
        insertNewTeamMember(bob);

        Task task1 = new Task("Lancement",12,getUniqueTeamMember((long)1),"ToDo","31/11/2021","c'est quand tu lance le projet");
        Task task2 = new Task("Dev",1,getUniqueTeamMember((long)2),"ToDo","31/09/2021","c'est quand tu écris le code");
        //Task task3 = new Task("Dev",1,getUniqueTeamMember((long)2),"InProgress","31/09/2021","c'est quand tu écris le code");
        insertNewTask(task1);
        insertNewTask(task2);
        //insertNewTask(task3);
        //Task task3 = new Task(3,"Dev","ToDo","31/09","Roger",2,"toot");
        //insertNewTask(task3);
        //passTaskToInProgress(task3);
    }

    public void close(){
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(this.getClass().getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS TaskToDo");
        db.execSQL("DROP TABLE IF EXISTS TaskDone");
        db.execSQL("DROP TABLE IF EXISTS TaskInProgress");
        db.execSQL("DROP TABLE IF EXISTS TeamMembers");
        onCreate(db);
    }

    public void cleanTable(){
        db.execSQL("DELETE FROM TaskToDo;");
        db.execSQL("DELETE FROM TaskDone;");
        db.execSQL("DELETE FROM TaskInProgress;");
        db.execSQL("DELETE FROM TeamMembers;");
    }
    //endregion Fin de section administration bdd

    //region Insertion dans les tables
    public void insertNewTask(Task t){
        ContentValues values = new ContentValues();
        values.put("nom",t.getNom());
        values.put("poid",t.getPoid());
        values.put("personneAssigne",t.getPersonneAssigne().getId());
        values.put("etat","ToDo");
        values.put("dateDeFin",t.getDateDeFin());
        values.put("description",t.getDescription());
        db.insert("TaskToDo",null,values);
    }

    public void insertNewTeamMember(TeamMember teamMember){
        ContentValues values = new ContentValues();
        values.put("nom",teamMember.getName());
        values.put("prenom",teamMember.getFirstname());
        db.insert("TeamMembers",null,values);
    }
    //endregion Fin insertion

    //region Affichage des tables
    public ArrayList<TeamMember> getAllMembers(){
        ArrayList<TeamMember> membersList = new ArrayList<TeamMember>();
        Cursor c = db.rawQuery("SELECT * FROM TeamMembers;",null);
        while (c.moveToNext()){
            TeamMember teamMember = new TeamMember(c.getLong(0),c.getString(2),c.getString(1));
            membersList.add(teamMember);
        }
        c.close();
        return membersList;
    }

    /*public ArrayList<String> getAllMembersName(){
        ArrayList<String> teamMemberName = new ArrayList<>();
        for (TeamMember teamMember:this.getAllMembers()
        ) {
            teamMemberName.add(teamMember.getName() + " " + teamMember.getFirstname());
        }
        return teamMemberName;
    }*/

    public TeamMember getUniqueTeamMember(Long id){
        TeamMember teamMember = null;
        // d parce qu'on sait pas si ça ne surcharge pas le curseur c
        Cursor d = db.rawQuery("SELECT * FROM TeamMembers WHERE id="+id+";",null);

        while (d.moveToNext()) {
            teamMember = new TeamMember(d.getLong(0),d.getString(2) , d.getString(1));
            return teamMember;
        }
        return teamMember;
    }

    public ArrayList<Task> getAllInProgressTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskInProgress;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),getUniqueTeamMember(c.getLong(3)),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllToDoTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskToDo;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),getUniqueTeamMember(c.getLong(3)),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllDoneTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskDone;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),getUniqueTeamMember(c.getLong(3)),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }
    //endregion

    public void updateTask(Task oldTask, Task newTask){
        ContentValues values = new ContentValues();
        values.put("id",oldTask.getId());
        values.put("nom",newTask.getNom());
        values.put("poid",newTask.getPoid());
        values.put("personneAssigne",newTask.getPersonneAssigne().getId());
        values.put("etat",newTask.getEtat());
        values.put("dateDeFin",newTask.getDateDeFin());
        values.put("description",newTask.getDescription());
        if(!newTask.getEtat().equals(oldTask.getEtat())){
            if(newTask.getEtat().equals("ToDo")){
                passTaskToDo(oldTask,values);
            } else if(newTask.getEtat().equals("InProgress")){
                passTaskInProgress(oldTask,values);
            } else if(newTask.getEtat().equals("Done")){
                passTaskDone(oldTask,values);
            }
        } else {
            if (oldTask.getEtat().equals("ToDo")) {
                db.update("TaskToDo", values, "id = ?", new String[]{String.valueOf(oldTask.getId())});
            } else if (oldTask.getEtat().equals("InProgress")) {
                db.update("TaskInProgress", values, "id = ?", new String[]{String.valueOf(oldTask.getId())});
            } else if (oldTask.getEtat().equals("Done")) {
                db.update("TaskDone", values, "id = ?", new String[]{String.valueOf(oldTask.getId())});
            }
        }
    }

    //region Changement d'état des taches

    public void passTaskInProgress(Task taskToSwitch,ContentValues nvValues){
        switch (taskToSwitch.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskInProgress",null,nvValues);
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskInProgress",null,nvValues);
            default:
                // already in InProgress Table
        }
    }

    public void passTaskDone(Task taskToSwitch,ContentValues nvValues){
        switch (taskToSwitch.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskDone",null,nvValues);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskDone",null,nvValues);
            default:
                // already in Done Table
        }
    }

    public void passTaskToDo(Task taskToSwitch,ContentValues nvValues){

        switch (taskToSwitch.getEtat()){
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskToDo",null,nvValues);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                db.insert("TaskToDo",null,nvValues);
            default:
                // already in ToDo Table
        }
    }
    //endregion

    //region Suppression d'entrée dans les tables
    public void deleteMember(TeamMember teamMember){
        db.delete("TeamMembers","id = ?",new String[]{String.valueOf(teamMember.getId())});
    }

    public void deleteTask(Task task){
        switch (task.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(task.getId())});
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(task.getId())});
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(task.getId())});
        }
    }

    //endregion


}
