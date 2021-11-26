package com.example.backlogprojectv2;

public class Task {
    private long id;
    private int poid;
    private String nom;
    private TeamMember personneAssigne;
    private String etat;
    private String dateDeFin;
    private String description;
    private String priority;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public TeamMember getPersonneAssigne() {
        return personneAssigne;
    }

    public void setPersonneAssigne(TeamMember personneAssigne) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Task(long id, String nom, int poid, TeamMember personneAssigne, String etat, String dateDeFin, String description,String priority){
        this.id = id;
        this.dateDeFin=dateDeFin;
        this.nom=nom;
        this.etat=etat;
        this.poid=poid;
        this.personneAssigne=personneAssigne;
        this.description=description;
        this.priority=priority;
        }

    public Task(String nom, int poid, TeamMember personneAssigne,String etat, String dateDeFin,String description,String priority){
        this.dateDeFin=dateDeFin;
        this.nom=nom;
        this.etat=etat;
        this.poid=poid;
        this.personneAssigne=personneAssigne;
        this.description=description;
        this.priority=priority;
    }
}
