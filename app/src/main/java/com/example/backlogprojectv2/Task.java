package com.example.backlogprojectv2;

public class Task {
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String gte; // je sais plus ce que c'est exactement (je vois r sur la feuille)

    public Task(long id,String nom, int poid, String personneAssigne,String etat, String dateDeFin){
        this.id = id;
        this.dateDeFin=dateDeFin;
        this.nom=nom;
        this.etat=etat;
        this.gte=gte;
        this.poid=poid;
        this.personneAssigne=personneAssigne;
        }



}
