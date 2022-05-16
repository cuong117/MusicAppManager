package com.example.musicupload.models;

import android.net.Uri;

public class Song {
    private String title;
    private String subTitle;
    private Uri link;

    public Song(){
    }

    public Song(String title, String subTitle, Uri link) {
        this.title = title;
        this.subTitle = subTitle;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Uri getLink() {
        return link;
    }

    public void setLink(Uri link) {
        this.link = link;
    }
}
