package com.example.musicupload.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
import com.example.musicupload.activity.MainActivity;
import com.example.musicupload.fragment.DevicemusicFragment;
import com.example.musicupload.models.Song;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private Context context;
    private List<Song> songList;

    public SongAdapter(@NonNull Context context, List<Song> songList) {
        super(context, R.layout.row, songList);
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        Song song = songList.get(position);
        TextView title = view.findViewById(R.id.music_name);
        TextView subTitle = view.findViewById(R.id.music_subtitle);
        ImageView upload = view.findViewById(R.id.btn_upload);
        title.setText(song.getTitle());
        subTitle.setText(song.getSubTitle());
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });
        return view;
    }
}
