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
        super(context,"backlogprojectv2",null,5);
        this.db = getWritableDatabase();
    }

    /*public void open(){
        this.db = this.getWritableDatabase();
    }*/

    @Override
    public void onCreate(SQLiteDatabase db){
        this.db = db;
        db.execSQL("CREATE TABLE TaskToDo (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskDone (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskInProgress (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TeamMembers (id INTEGER PRIMARY KEY AUTOINCREMENT, prenom TEXT, nom TEXT);");


        // donnée test
        Task task1 = new Task(2,"Lancement",12,"Bob","ToDo","31/11/2021","c'est quand tu lance le projet");
        Task task2 = new Task(1,"Dev",1,"Rogers","ToDo","31/09/2021","c'est quand tu écris le code");
        insertNewTask(task1);
        insertNewTask(task2);
        insertNewTeamMember(new TeamMember(1,"Alice","tamere"));
        insertNewTeamMember(new TeamMember(2,"Alice","tamere"));
        //Task task3 = new Task(3,"Dev","ToDo","31/09","Roger",2,"toot");
        //insertNewTask(task3);
        //passTaskToInProgress(task3);
    }

    public void close(){
        db.close();
    }

    public void insertNewTask(Task t){
        ContentValues values = new ContentValues();
        values.put("nom",t.getNom());
        values.put("poid",t.getPoid());
        values.put("personneAssigne",t.getPersonneAssigne());
        values.put("etat",t.getEtat());
        values.put("dateDeFin",t.getDateDeFin());
        values.put("description",t.getDescription());
        switch (t.getEtat()){
            case "InProgress":
                db.insert("TaskInProgress",null,values);
            case "Done":
                db.insert("TaskDone",null,values);
            default:
                db.insert("TaskToDo",null,values);
        }
    }

    public void insertNewTeamMember(TeamMember teamMember){
        ContentValues values = new ContentValues();
        values.put("nom",teamMember.getName());
        values.put("prenom",teamMember.getFirstname());
        values.put("id",teamMember.getId());
        db.insert("TeamMembers",null,values);
    }

    public void passTaskToInProgress(Task taskToSwitch){
        ContentValues values = new ContentValues();
        switch (taskToSwitch.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","InProgress");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskInProgress",null,values);
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","InProgress");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskInProgress",null,values);
            default:
                // already in InProgress Table
        }
    }

    public void passTaskDone(Task taskToSwitch){
        ContentValues values = new ContentValues();
        switch (taskToSwitch.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","Done");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","Done");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            default:
                // already in Done Table
        }
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

    public ArrayList<TeamMember> getAllMembers(){
        ArrayList<TeamMember> membersList = new ArrayList<TeamMember>();
        Cursor c = db.rawQuery("SELECT * FROM TeamMembers;",null);
        while (c.moveToNext()){
            TeamMember teamMember = new TeamMember(c.getLong(0),c.getString(1),c.getString(2));
            membersList.add(teamMember);
        }
        c.close();
        return membersList;
    }

    public ArrayList<Task> getAllInProgressTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskInProgress;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllToDoTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskToDo;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllDoneTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskDone;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

}
