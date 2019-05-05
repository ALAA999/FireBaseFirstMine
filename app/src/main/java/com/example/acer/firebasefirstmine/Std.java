package com.example.acer.firebasefirstmine;

/**
 * Created by acer on 1/18/2018.
 */

public class Std {
    String name;
    int id;

    public Std(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public Std(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
