package com.example.musicupload.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicupload.activity.LogIn;
import com.example.musicupload.activity.MainActivity;
import com.example.musicupload.adapter.SongAdapter;
import com.example.musicupload.adapter.UserAdapter;
import com.example.musicupload.databinding.DeviceMusicFragmentBinding;
import com.example.musicupload.databinding.FormUpdateFragmentBinding;
import com.example.musicupload.databinding.UserFragmentBinding;
import com.example.musicupload.models.Song;
import com.example.musicupload.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private ListView userList;
    private UserFragmentBinding binding;
    private DatabaseReference db;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = UserFragmentBinding.inflate(inflater, container, false);
        userList = binding.userList;
        userList.setAdapter(getUser());
        return binding.getRoot();

    }

    private UserAdapter getUser(){
        List<User> users = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    Log.v("ListUser", user.toString());
                    Log.v("List", postSnapshot.toString() + "\n");
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return new UserAdapter(getContext(), users);
    }
}
