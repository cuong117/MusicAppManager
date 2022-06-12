package com.example.musicupload.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
import com.example.musicupload.adapter.CloudSongAdapter;
import com.example.musicupload.databinding.CloudFragmentBinding;
import com.example.musicupload.models.Song;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CloudFragment extends Fragment {

    private CloudFragmentBinding binding;
    private DatabaseReference db;
    private ListView cloudSongList;
    private ImageView playing;
    private MediaPlayer mediaPlayer;
    private CloudSongAdapter adapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = CloudFragmentBinding.inflate(inflater, container, false);
        cloudSongList = binding.songListCloud;
        db = FirebaseDatabase.getInstance().getReference();
        adapter = new CloudSongAdapter(getContext(), new ArrayList<>());
        cloudSongList.setAdapter(adapter);
        getMusic();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                mediaPlayer.start();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cloudSongList.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (view1.getId() == R.id.btn_delete){
                if (playing != null){
                    playing.setVisibility(View.INVISIBLE);
                    mediaPlayer.stop();
                    playing = null;
                }
                onSongDelete((Song) adapterView.getItemAtPosition(i));
            } else if (view1.getId() == R.id.btn_update){
                if(mediaPlayer != null){
                    mediaPlayer.release();
                }
                switchToFormUpdate((Song)adapterView.getItemAtPosition(i));
            } else {
                ImageView play = view1.findViewById(R.id.img_playing);
                if (playing != null && playing == play){
                    playing.setVisibility(View.INVISIBLE);
                    playing = null;
                    mediaPlayer.stop();
                    return;
                } else if (playing != null){
                    playing.setVisibility(View.INVISIBLE);
                }
                play.setVisibility(View.VISIBLE);
                playing = play;
                Song s = (Song) adapterView.getItemAtPosition(i);
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(s.getLink());
                    createProgressDialog("Preparing music for you...");
                    progressDialog.show();
                    mediaPlayer.prepareAsync();
//                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMusic(){
        createProgressDialog("Loading...");
        progressDialog.show();
        DatabaseReference curDB = db.child("songs");
        curDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for (DataSnapshot dt: snapshot.getChildren()){
                    Song s = dt.getValue(Song.class);
                    s.setKey(dt.getKey());
                    if (s.getSubTitle() == null){
                        s.setSubTitle("<unknown>");
                    }
                    adapter.insert(s, adapter.getCount());
                }
                adapter.sort();
                adapter.notifyDataSetChanged();
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onSongDelete(Song s){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Song");
        builder.setMessage("Delete \"" + s.getTitle() + "\" ?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createProgressDialog("Deleting...");
                progressDialog.show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(s.getLink());
                storageReference.delete().addOnSuccessListener(unused -> {
                    db.child("songs").child(s.getKey()).removeValue();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createProgressDialog(String msg){
        progressDialog = new ProgressDialog(getContext(), 5);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
    }

    private void switchToFormUpdate(Song s){
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", s);
        getParentFragmentManager().setFragmentResult("song", bundle);
        NavHostFragment.findNavController(CloudFragment.this)
                .navigate(R.id.nav_cloud_to_FormUpdateFragment);
    }
}
