package com.example.musicupload.models;

import java.io.Serializable;

public class Song extends SongItem implements Comparable<Song>, Serializable{
    private String key;

    public Song(){
    }

    public Song(String title, String subTitle, String link) {
        super(title, subTitle, link);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Song song) {
        return this.getTitle().compareTo(song.getTitle());
    }
}
