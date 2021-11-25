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
        super(context,"backlogprojectv2",null,12);
        this.db = getWritableDatabase();
    }

    // Administration de la base de donnée
    @Override
    public void onCreate(SQLiteDatabase db){
        this.db = db;
        db.execSQL("CREATE TABLE TaskToDo (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskDone (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TaskInProgress (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nom TEXT, poid INTEGER, personneAssigne TEXT, etat TEXT, dateDeFin TEXT, description TEXT);");
        db.execSQL("CREATE TABLE TeamMembers (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, prenom TEXT, nom TEXT);");


        // donnée test
        TeamMember alice = new TeamMember("Alice","tamere");
        TeamMember bob = new TeamMember("Bob","tamere");
        insertNewTeamMember(alice);
        insertNewTeamMember(bob);

        Task task1 = new Task("Lancement",12,getUniqueTeamMember((long)1),"ToDo","31/11/2021","c'est quand tu lance le projet");
        Task task2 = new Task("Dev",1,getUniqueTeamMember((long)2),"ToDo","31/09/2021","c'est quand tu écris le code");
        insertNewTask(task1);
        insertNewTask(task2);
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

        db.rawQuery("DELETE FROM TaskToDo;",null);
        db.rawQuery("DELETE FROM TaskDone;",null);
        db.rawQuery("DELETE FROM TaskInProgress;",null);
        db.rawQuery("DELETE FROM TeamMembers;",null);
    }
    // Fin de section administration bdd

    /* Insertion dans les tables */
    public void insertNewTask(Task t){
        ContentValues values = new ContentValues();
        values.put("nom",t.getNom());
        values.put("poid",t.getPoid());
        values.put("personneAssigne",t.getPersonneAssigne().getId());
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
        db.insert("TeamMembers",null,values);
    }
    // Fin insertion

    // Affichage des tables
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

    public TeamMember getUniqueTeamMember(Long id){
        TeamMember teamMember = null;
        // d parce qu'on sait pas si ça ne surcharge pas le curseur c
        Cursor d = db.rawQuery("SELECT * FROM TeamMembers WHERE id="+id+";",null);
        int rowCount = d.getCount();

        while (d.moveToNext()) {
            teamMember = new TeamMember(d.getLong(0), d.getString(1), d.getString(2));
            return teamMember;
        }
        return teamMember;
    }

    public ArrayList<Task> getAllInProgressTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskInProgress;",null);
        while (c.moveToNext()){
            //TeamMember teamMember = getUniqueTeamMember(c.getLong(3));
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
            long id = c.getLong(3);
            String nameTask = c.getString(1);
            long idTask = c.getLong(0);

            TeamMember teamMember = getUniqueTeamMember(id);
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),teamMember,c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    public ArrayList<Task> getAllDoneTask(){
        ArrayList<Task> taches = new ArrayList<Task>();
        Cursor c = db.rawQuery("SELECT * FROM TaskDone;",null);
        while (c.moveToNext()){
            TeamMember teamMember = getUniqueTeamMember(c.getLong(3));
            Task task = new Task(c.getLong(0),c.getString(1),c.getInt(2),teamMember,c.getString(4),c.getString(5),c.getString(6));
            taches.add(task);
        }
        c.close();
        return taches;
    }

    // Changement d'état des taches

    public void passTaskToInProgress(Task taskToSwitch){
        ContentValues values = new ContentValues();
        switch (taskToSwitch.getEtat()){
            case "ToDo":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
                values.put("etat","InProgress");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskInProgress",null,values);
            case "Done":
                db.delete("TaskDone","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
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
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
                values.put("etat","Done");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
                values.put("etat","Done");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            default:
                // already in Done Table
        }
    }

    public void passTaskToDo(Task taskToSwitch){
        ContentValues values = new ContentValues();
        switch (taskToSwitch.getEtat()){
            case "Done":
                db.delete("TaskToDo","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
                values.put("etat","ToDo");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            case "InProgress":
                db.delete("TaskInProgress","id = ?",new String[]{String.valueOf(taskToSwitch.getId())});
                values.put("nom",taskToSwitch.getNom());
                values.put("poid",taskToSwitch.getPoid());
                values.put("personneAssigne",taskToSwitch.getPersonneAssigne().getId());
                values.put("etat","ToDo");
                values.put("dateDeFin",taskToSwitch.getDateDeFin());
                values.put("description",taskToSwitch.getDescription());
                db.insert("TaskDone",null,values);
            default:
                // already in ToDo Table
        }
    }

    public void deleteMember(TeamMember teamMember){
        db.delete("TeamMembers","id = ?",new String[]{String.valueOf(teamMember.getId())});
    }
    // Fin de changement d'état des tâches


}
