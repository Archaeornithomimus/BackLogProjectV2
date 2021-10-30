package com.example.backlogprojectv2;

public class Task {
    private int poid;
    private String nom;
    private String personneAssigne;
    private String etat;
    private String dateDeFin;
    private String gte; // je sais plus ce que c'est exactement (je vois r sur la feuille)

    public Task(String nom, String etat, String dateDeFin, String personneAssigne, int poid, String gte){
        this.dateDeFin =dateDeFin;
        this.nom = nom;
        this.etat=etat;
        this.gte=gte;
        this.poid=poid;
        this.personneAssigne=personneAssigne;
    }



}
