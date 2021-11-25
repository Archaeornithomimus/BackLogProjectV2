package com.example.backlogprojectv2;

public class TeamMember {
    private long id;
    private String name;
    private String firstname;

    public TeamMember(long id, String name, String firstname) {
        this.firstname = firstname;
        this.name = name;
        this.id = id;
    }

    public TeamMember(String name, String firstname) {
        this.firstname = firstname;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getNameMember(){
        return this.name + " " + this.firstname;
    }
}
