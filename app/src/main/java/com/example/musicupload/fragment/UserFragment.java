package com.example.musicupload.fragment;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
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

import java.io.IOException;
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
        getUser();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("View", view.toString());
                if (view.getId() == R.id.btn_update){
                    User user = (User) adapterView.getItemAtPosition(i);
                    String name = user.getName();
                    String email = user.getEmail();
                    String level = user.isAdmin()?"Admin":"User";
                    Toast.makeText(getContext(), "User update button clicked", Toast.LENGTH_SHORT).show();
                } else if (view.getId() == R.id.btn_delete){
                    User user = (User) adapterView.getItemAtPosition(i);
                    String name = user.getName();
                    String email = user.getEmail();
                    String level = user.isAdmin()?"Admin":"User";
                    Toast.makeText(getContext(), "Delete button clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getUser(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting user...");
        progressDialog.show();
        List<User> users = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    Log.v("ListUser", user.toString());
                    Log.v("List", postSnapshot.toString() + "\n");
                    users.add(user);
                }
                userList.setAdapter(new UserAdapter(getContext(), users));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
