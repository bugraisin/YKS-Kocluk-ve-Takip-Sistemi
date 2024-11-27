package models;

public class Student {
    private int studentID;
    private String firstName;
    private String midName;
    private String lastName;
    private String email;
    private String phoneNo;
    private int classNo;
    private String goalUni;
    private String goalMajor;

    // Getters ve Setters
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getClassNo() {
        return classNo;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    public String getGoalUni() {
        return goalUni;
    }

    public void setGoalUni(String goalUni) {
        this.goalUni = goalUni;
    }

    public String getGoalMajor() {
        return goalMajor;
    }

    public void setGoalMajor(String goalMajor) {
        this.goalMajor = goalMajor;
    }
}
