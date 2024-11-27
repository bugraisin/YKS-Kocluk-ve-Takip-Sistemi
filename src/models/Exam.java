package models;

import java.sql.Date;
import java.sql.Time;

public class Exam {
    private int examID;
    private int studentID;
    private Time examTime;
    private Date examDate;

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public Time getExamTime() {
        return examTime;
    }

    public void setExamTime(Time examTime) {
        this.examTime = examTime;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }
}
