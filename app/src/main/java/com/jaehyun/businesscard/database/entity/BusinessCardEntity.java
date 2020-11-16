package com.jaehyun.businesscard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "business_card")
public class BusinessCardEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;

    private String name;
    private String email;
    private String tel;
    private String mobile;
    private String team;
    private String position;
    private String address;
    private String setFax;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSetFax() {
        return setFax;
    }

    public void setSetFax(String setFax) {
        this.setFax = setFax;
    }

    @Override
    public String toString() {
        return "BusinessCardEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", mobile='" + mobile + '\'' +
                ", team='" + team + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                ", setFax='" + setFax + '\'' +
                '}';
    }
}
