package com.example.musicupload.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
import com.example.musicupload.activity.MainActivity;
import com.example.musicupload.adapter.SongAdapter;
import com.example.musicupload.databinding.DeviceMusicFragmentBinding;
import com.example.musicupload.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DevicemusicFragment extends Fragment {

    private DeviceMusicFragmentBinding binding;
    private ListView songList;
    private ImageView playing;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = DeviceMusicFragmentBinding.inflate(inflater, container, false);
        songList = binding.songList;
        mediaPlayer = new MediaPlayer();
        setSongList();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getId() == R.id.btn_upload){
                    Song song = (Song) adapterView.getItemAtPosition(i);
                    String name = song.getTitle();
                    String subTitle = song.getSubTitle();
                    String path = song.getLink();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", name);
                    bundle.putString("subTitle", subTitle);
                    bundle.putString("path",path);
                    if (mediaPlayer != null){
                        mediaPlayer.release();
                    }
                    getParentFragmentManager().setFragmentResult("info", bundle);
                    NavHostFragment.findNavController(DevicemusicFragment.this)
                            .navigate(R.id.action_DeviceFragment_to_FormUploadFragment);
                } else {
                    ImageView imgView = view.findViewById(R.id.img_playing);
                    if(playing != null && playing == imgView){
                        playing.setVisibility(View.INVISIBLE);
                        playing = null;
                        mediaPlayer.stop();
                        return;
                    }else if (playing != null){
                        playing.setVisibility(View.INVISIBLE);
                    }
                    imgView.setVisibility(View.VISIBLE);
                    playing = imgView;
                    Song song = (Song) adapterView.getItemAtPosition(i);
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(song.getLink());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
    }

    private SongAdapter getMusic(){
        ContentResolver contentResolver = ((MainActivity)getActivity()).getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            Log.v("version1", "S");
            cursor = contentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + "!= 0 and " +

                    MediaStore.Audio.Media.IS_ALARM + " = 0 and " + MediaStore.Audio.Media.IS_RECORDING + " = 0 and " +
                    MediaStore.Audio.Media.IS_NOTIFICATION + " = 0 and " + MediaStore.Audio.Media.IS_RINGTONE + " = 0"
                    , null, MediaStore.Audio.Media.TITLE + " ASC");
        } else {
            Log.v("version1", "not S");
            cursor = contentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + "!= 0 and " +
                            MediaStore.Audio.Media.IS_ALARM + " = 0 and " +
                            MediaStore.Audio.Media.IS_NOTIFICATION + " = 0 and " + MediaStore.Audio.Media.IS_RINGTONE + " = 0"
                    , null, MediaStore.Audio.Media.TITLE + " ASC");
        }
        List<Song> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            int title =cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//            Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    cursor.getInt(path));
            do{
                String uriSong = cursor.getString(path);
                if (!uriSong.toLowerCase().contains("recorder")){
                    songs.add(new Song(cursor.getString(title), cursor.getString(artist), uriSong));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return new SongAdapter(getContext(), songs);
    }


    public void setSongList(){
        songList.setAdapter(getMusic());
    }
}