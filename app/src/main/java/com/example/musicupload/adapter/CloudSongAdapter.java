package com.example.musicupload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicupload.R;
import com.example.musicupload.models.Song;

import java.util.List;

public class CloudSongAdapter extends ArrayAdapter<Song> {
    private Context context;
    private List<Song> songList;

    public CloudSongAdapter(@NonNull Context context, List<Song> songList) {
        super(context, R.layout.row_cloud_song, songList);
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_cloud_song, parent, false);
        TextView title = view.findViewById(R.id.music_name);
        TextView subTitle = view.findViewById(R.id.music_subtitle);
        ImageView delete = view.findViewById(R.id.btn_delete);
        ImageView update = view.findViewById(R.id.btn_update);
        Song song = songList.get(position);
        title.setText(song.getTitle());
        subTitle.setText(song.getSubTitle());

        delete.setOnClickListener(view1 -> {
            ((ListView)parent).performItemClick(view1, position, 0);
        });

        update.setOnClickListener(view1 -> {
            ((ListView)parent).performItemClick(view1, position, 0);
        });

        return view;
    }
}
