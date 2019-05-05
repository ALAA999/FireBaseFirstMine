package com.example.acer.firebasefirstmine.Models;

/**
 * Created by acer on 3/11/2018.
 */

public class Posts {
    String Title, Details, Image, Username, Uid;

    public Posts() {

    }

    public Posts(String title, String details, String image, String username, String uid) {
        Title = title;
        Details = details;
        Image = image;
        Username = username;
        Uid = uid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
