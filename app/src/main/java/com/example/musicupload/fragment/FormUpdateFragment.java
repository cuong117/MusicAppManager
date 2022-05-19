package com.example.musicupload.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicupload.R;
import com.example.musicupload.databinding.FormUpdateFragmentBinding;
import com.example.musicupload.models.Song;
import com.example.musicupload.models.SongItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormUpdateFragment extends Fragment {

    private FormUpdateFragmentBinding binding;
    private Button update_btn;
    private EditText title;
    private EditText subTitle;
    private Song song;
    private DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FormUpdateFragmentBinding.inflate(inflater, container, false);
        update_btn = binding.update;
        title = binding.editTitle;
        subTitle = binding.editArtist;
        db = FirebaseDatabase.getInstance().getReference();
        getParentFragmentManager().setFragmentResultListener("song", this, (requestKey, result) -> {
            song = (Song) result.getSerializable("info");
            title.setText(song.getTitle());
            subTitle.setText(song.getSubTitle());
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update_btn.setOnClickListener(view1 -> {
            SongItem item = new SongItem(title.getText().toString(), subTitle.getText().toString(), song.getLink());
            db.child("songs").child(song.getKey()).setValue(item);
            Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(FormUpdateFragment.this)
                    .navigate(R.id.FormUpdateFragment_to_nav_cloud);
        });
    }
}
