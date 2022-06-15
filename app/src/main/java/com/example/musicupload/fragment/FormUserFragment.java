package com.example.musicupload.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.musicupload.adapter.UserAdapter;
import com.example.musicupload.databinding.FormUploadFragmentBinding;
import com.example.musicupload.databinding.FormUserFragmentBinding;
import com.example.musicupload.models.Song;
import com.example.musicupload.models.SongItem;
import com.example.musicupload.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FormUserFragment extends Fragment {
    private FormUserFragmentBinding binding;
    private TextView name;
    private TextView email;
    private CheckBox level;
    private Button update;
    private StorageReference store;
    private StorageTask task;
    private DatabaseReference db;
    private User user;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FormUserFragmentBinding.inflate(inflater, container, false);
        name = binding.editUserName;
        email = binding.email;
        level = binding.userLevel;
        update = binding.update;
        db = FirebaseDatabase.getInstance().getReference().child("user");
        store = FirebaseStorage.getInstance().getReference().child("user");
        getParentFragmentManager().setFragmentResultListener("user",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                user = (User) result.getSerializable("info");
                String _name = user.getName();
                Boolean _level = user.isAdmin();
                String _email = user.getEmail();
                name.setText(_name);
                email.setText(_email);
                level.setChecked(false);
                if (_level == true) {
                    level.setChecked(true);
                }

            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getContext(), 5);
                progressDialog.setMessage("Updating user...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signInWithEmailAndPassword(user.getEmail(), user.getPass())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference db = FirebaseDatabase.getInstance()
                                            .getReference().child("user")
                                            .child(fAuth.getCurrentUser().getUid());
                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Log.v("data", snapshot.getRef().child("name").toString());
                                            snapshot.getRef().child("name").setValue(user.getName());
                                            if (level.isChecked()== true) {
                                            snapshot.getRef().child("admin").setValue(true);
                                            } else {
                                                snapshot.getRef().child("admin").setValue(false);
                                            }
                                            Toast.makeText(getContext(), "Update success!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void updateUser() {

    }
}
