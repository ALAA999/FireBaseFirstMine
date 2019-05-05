package com.example.acer.firebasefirstmine;

/**
 * Created by acer on 1/20/2018.
 */

public class Rtd {
    String name;
    String num;
    String id;

    public Rtd(){

    }
    public Rtd(String name, String num,String id){
this.name = name;
this.num = num;
this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
