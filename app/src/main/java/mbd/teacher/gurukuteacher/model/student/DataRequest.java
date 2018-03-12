package mbd.teacher.gurukuteacher.model.student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naufal on 20/02/2018.
 */

public class DataRequest {
    @SerializedName("bookID")
    @Expose
    private Integer bookID;
    @SerializedName("teacherID")
    @Expose
    private Integer teacherID;
    @SerializedName("studentID")
    @Expose
    private Integer studentID;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("Student")
    @Expose
    private Student student;

    public DataRequest(Integer bookID, Integer status, Integer duration, String location, String date, String time, Student student) {
        this.bookID = bookID;
        this.status = status;
        this.duration = duration;
        this.location = location;
        this.date = date;
        this.time = time;
        this.student = student;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public Integer getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(Integer teacherID) {
        this.teacherID = teacherID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
