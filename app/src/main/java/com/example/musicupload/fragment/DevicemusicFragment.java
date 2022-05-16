package com.example.musicupload.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
import com.example.musicupload.activity.MainActivity;
import com.example.musicupload.adapter.SongAdapter;
import com.example.musicupload.databinding.DeviceMusicFragmentBinding;
import com.example.musicupload.models.Song;

import java.util.ArrayList;
import java.util.List;


public class DevicemusicFragment extends Fragment {

    private DeviceMusicFragmentBinding binding;
    private ListView songList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = DeviceMusicFragmentBinding.inflate(inflater, container, false);
        songList = binding.songList;
        setSongList();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("aaaa", adapterView.getItemAtPosition(i) + "");
                if (view.getId() == R.id.btn_upload){
                    Song song = (Song) adapterView.getItemAtPosition(i);
                    String name = song.getTitle();
                    String subTitle = song.getSubTitle();
                    Uri path = song.getLink();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", name);
                    bundle.putString("subTitle", subTitle);
                    bundle.putParcelable("path",path);
//                    FormUploadFragment formUploadFragment = new FormUploadFragment();
                    getParentFragmentManager().setFragmentResult("info", bundle);
                    NavHostFragment.findNavController(DevicemusicFragment.this)
                            .navigate(R.id.action_DeviceFragment_to_FormFragment);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private SongAdapter getMusic(){
        ContentResolver contentResolver = ((MainActivity)getActivity()).getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Song> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            int title =cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int path = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getInt(path));
            int count = 0;
            do{
                songs.add(new Song(cursor.getString(title), cursor.getString(artist), audioUri));
                count++;
            }while (cursor.moveToNext());
            Log.v("bbbb", count + "");
            cursor.close();
        }
        return new SongAdapter(getContext(), songs);
    }

    public void setSongList(){
        songList.setAdapter(getMusic());
    }
}