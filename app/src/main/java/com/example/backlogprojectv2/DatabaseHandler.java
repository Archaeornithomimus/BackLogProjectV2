package com.example.backlogprojectv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;


    public DatabaseHandler(Context context){
        super(context,"backlogprojectv2",null,2);
        this.db = getWritableDatabase();
    }

    /*public void open(){
        this.db = this.getWritableDatabase();
    }*/

    @Override
    public void onCreate(SQLiteDatabase db){
        this.db = db;
        db.execSQL("CREATE TABLE TaskToDo (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT);");
        db.execSQL("CREATE TABLE TaskDone (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT);");
        db.execSQL("CREATE TABLE TaskInProgress (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT);");
        db.execSQL("CREATE TABLE TeamMembers (id INTEGER PRIMARY KEY AUTOINCREMENT, prenom TEXT, nom TEXT, personneAssigne TEXT, etat TEXT, dateDeFin TEXT);");

        // donnée test
        Task task1 = new Task(2,"Lancement",12,"Bob","ToDo","12/09/2021");
        Task task2 = new Task(1,"Dev",1,"Rogers","ToDo","31/09/2021");
        insertNewTask(task1);
        insertNewTask(task2);

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
        switch (t.getEtat()){
            case "InProgress":
                db.insert("TaskInProgress",null,values);
            case "Done":
                db.insert("TaskDone",null,values);
            default:
                db.insert("TaskToDo",null,values);
        }
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
                db.insert("TaskInProgress",null,values);
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","InProgress");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
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
                db.insert("TaskDone",null,values);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne());
                values.put("etat","Done");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
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

    public ArrayList<Task> getAllInProgressTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskInProgress;",null);
        c.moveToFirst();
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllToDoTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskToDo;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllDoneTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskDone;",null);
        while (c.moveToNext()){
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5));
            taches.add(task);
        }
        c.close();
        return taches;
    }




}
