package com.example.musicupload.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private UserAdapter adapter;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = UserFragmentBinding.inflate(inflater, container, false);
        userList = binding.userList;
        adapter = new UserAdapter(getContext(), new ArrayList<>());
        userList.setAdapter(adapter);
        getUser();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();
                User user = (User) adapterView.getItemAtPosition(i);
                String email = user.getEmail();
                if (view.getId() == R.id.img_user_update){
                    switchToFormUpdate(user);
                } else if (view.getId() == R.id.img_user_delete){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete user?")
                            .setMessage("Are you sure you want to delete this user?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUser(email, user.getPass());

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    public void deleteUser(String email, String password) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser delete_user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user").child(fAuth.getCurrentUser().getUid());
                            delete_user.delete();
                            db.removeValue();
                        }
                        }
                    });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                adapter.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    adapter.insert(user, adapter.getCount());
                }
                adapter.notifyDataSetChanged();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void switchToFormUpdate(User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", user);
        getParentFragmentManager().setFragmentResult("user", bundle);
        NavHostFragment.findNavController(UserFragment.this)
                .navigate(R.id.nav_user_to_FormUserFragment);
    }
}
