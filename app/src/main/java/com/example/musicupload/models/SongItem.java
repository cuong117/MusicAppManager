package com.example.musicupload.models;

public class SongItem {
    private String title;
    private String subTitle;
    private String link;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public SongItem(String title, String subTitle, String link) {
        this.title = title;
        this.subTitle = subTitle;
        this.link = link;
    }

    public SongItem(){
    }
}
