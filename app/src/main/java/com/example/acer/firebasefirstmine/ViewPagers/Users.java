package com.example.acer.firebasefirstmine.ViewPagers;

public class Users {
    private String name , image , UserID;
    // Make sure that varailbes are private and when you call them you use getters and setters
    public Users(String name, String image, String userID) {
        this.name = name;
        this.image = image;
        UserID = userID;
    }

    public Users(){

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
