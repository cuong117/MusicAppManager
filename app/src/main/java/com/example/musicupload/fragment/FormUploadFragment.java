package com.example.musicupload.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.musicupload.databinding.FormUploadFragmentBinding;
import com.example.musicupload.models.Song;
import com.example.musicupload.models.SongItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


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
                Log.v("send", "aaaaa");
                String _title = result.getString("title");
                String _subTitle = result.getString("subTitle");
                title.setText(_title);
                subtitle.setText(_subTitle);
                song = new Song(_title, _subTitle, result.getParcelable("path"));
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference storageReference = store.child(System.currentTimeMillis() + "");
                task = storageReference.putFile(song.getLink());
                task.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                SongItem songItem = new SongItem();
                                songItem.setTitle(song.getTitle());
                                songItem.setSubTitle(song.getSubTitle());
                                songItem.setLink(uri.toString());
                                String id = db.push().getKey();
                                db.child(id).setValue(songItem);
                                Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
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