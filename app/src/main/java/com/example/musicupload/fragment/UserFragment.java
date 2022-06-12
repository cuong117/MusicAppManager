package com.example.musicupload.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicupload.databinding.FormUpdateFragmentBinding;
import com.example.musicupload.databinding.UserFragmentBinding;
import com.example.musicupload.models.Song;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {

    private UserFragmentBinding binding;
    private DatabaseReference db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = UserFragmentBinding.inflate(inflater, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        getParentFragmentManager().setFragmentResultListener("song", this, (requestKey, result) -> {
        });
        return binding.getRoot();
    }
}
