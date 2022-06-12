package com.example.musicupload.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.musicupload.R;
import com.example.musicupload.databinding.FormUploadFragmentBinding;
import com.example.musicupload.models.Song;
import com.example.musicupload.models.SongItem;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class FormUploadFragment extends Fragment {

    private FormUploadFragmentBinding binding;
    private EditText title;
    private EditText subtitle;
    private Button upload;
    private StorageReference store;
    private StorageTask task;
    private DatabaseReference db;
    private Song song;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FormUploadFragmentBinding.inflate(inflater, container, false);
        title = binding.editTitle;
        subtitle = binding.editArtist;
        upload = binding.upload;
        db = FirebaseDatabase.getInstance().getReference().child("songs");
        store = FirebaseStorage.getInstance().getReference().child("songs");
        getParentFragmentManager().setFragmentResultListener("info",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String _title = result.getString("title");
                String _subTitle = result.getString("subTitle");
                title.setText(_title);
                subtitle.setText(_subTitle);
                song = new Song(_title, _subTitle, result.getString("path"));
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getContext(), 5);
                progressDialog.setMessage("Uploading... 0%");
                progressDialog.setCancelable(false);
                progressDialog.show();
                StorageReference storageReference = store.child(System.currentTimeMillis() + "");
                task = storageReference.putFile(Uri.fromFile(new File(song.getLink())));
                task.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.v("putfile", "success");
                                SongItem songItem = new SongItem(title.getText().toString()
                                        , subtitle.getText().toString(), uri.toString());
                                String id = db.push().getKey();
                                db.child(id).setValue(songItem);
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                task.addOnProgressListener(new OnProgressListener<TaskSnapshot>() {

                    @Override
                    public void onProgress(@NonNull TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading... " + (int)progress + "%");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}