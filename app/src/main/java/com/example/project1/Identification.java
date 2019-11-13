package com.example.project1;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "identification")
public class Identification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "GTCNtype")
    private String GTCNtype;

    @ColumnInfo(name = "GTCNnumber")
    private String GTCNnumber;

    @ColumnInfo(name = "fullname")
    private String fullname;

    @ColumnInfo(name = "dayOfBirth")
    private String dayOfBirth;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "nativePlace")
    private String nativePlace;

    @ColumnInfo(name = "dated")
    private String dated;

    @ColumnInfo(name = "place")
    private String place;

    @ColumnInfo(name = "frontImage")
    private String frontImage;

    @ColumnInfo(name = "backSideImage")
    private String backSideImage;

    public Identification() {
    }

    public Identification(String GTCNtype, String GTCNnumber, String fullname, String dayOfBirth, String gender, String nativePlace, String dated, String place, String frontImage, String backSideImage) {
        this.GTCNtype = GTCNtype;
        this.GTCNnumber = GTCNnumber;
        this.fullname = fullname;
        this.dayOfBirth = dayOfBirth;
        this.gender = gender;
        this.nativePlace = nativePlace;
        this.dated = dated;
        this.place = place;
        this.frontImage = frontImage;
        this.backSideImage = backSideImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGTCNtype() {
        return GTCNtype;
    }

    public void setGTCNtype(String GTCNtype) {
        this.GTCNtype = GTCNtype;
    }

    public String getGTCNnumber() {
        return GTCNnumber;
    }

    public void setGTCNnumber(String GTCNnumber) {
        this.GTCNnumber = GTCNnumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackSideImage() {
        return backSideImage;
    }

    public void setBackSideImage(String backSideImage) {
        this.backSideImage = backSideImage;
    }

    @Override
    public String toString() {
        return "Identification{" +
                "id=" + id +
                ", GTCNtype='" + GTCNtype + '\'' +
                ", GTCNnumber='" + GTCNnumber + '\'' +
                ", fullname='" + fullname + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", dated='" + dated + '\'' +
                ", place='" + place + '\'' +
                ", frontImage='" + frontImage + '\'' +
                ", backSideImage='" + backSideImage + '\'' +
                '}';
    }
}
