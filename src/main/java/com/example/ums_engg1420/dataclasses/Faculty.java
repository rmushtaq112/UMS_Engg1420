package com.example.ums_engg1420.dataclasses;

public class Faculty {
    private String name;
    private String degree;
    private String email;
    private String office;
    private String researchInterest;

    public Faculty(String name, String degree, String email, String office, String researchInterest) {
        this.name = name;
        this.degree = degree;
        this.email = email;
        this.office = office;
        this.researchInterest = researchInterest;
    }

    public String getName() { return name; }
    public String getDegree() { return degree; }
    public String getEmail() { return email; }
    public String getOffice() { return office; }
    public String getResearchInterest() { return researchInterest; }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", email='" + email + '\'' +
                ", office='" + office + '\'' +
                ", researchInterest='" + researchInterest + '\'' +
                '}';
    }
}
