package com.example.backlogprojectv2;

public class Task {
    private int poid;
    private String nom;
    private String personneAssigne;
    private String etat;
    private String dateDeFin;

    public int getPoid() {
        return poid;
    }

    public void setPoid(int poid) {
        this.poid = poid;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPersonneAssigne() {
        return personneAssigne;
    }

    public void setPersonneAssigne(String personneAssigne) {
        this.personneAssigne = personneAssigne;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDateDeFin() {
        return dateDeFin;
    }

    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public String getGte() {
        return gte;
    }

    public void setGte(String gte) {
        this.gte = gte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String gte; // je sais plus ce que c'est exactement (je vois r sur la feuille)
    private int id;

    public Task(String nom, String etat, String dateDeFin, String personneAssigne, int poid, String gte){
        this.dateDeFin =dateDeFin;
        this.nom = nom;
        this.etat=etat;
        this.gte=gte;
        this.poid=poid;
        this.personneAssigne=personneAssigne;
    }



}
