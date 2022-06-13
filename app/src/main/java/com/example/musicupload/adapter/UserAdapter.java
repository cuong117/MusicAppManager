package com.example.musicupload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicupload.R;
import com.example.musicupload.models.User;
import com.example.musicupload.models.Song;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> userList;

    public UserAdapter(@NonNull Context context, List<User> userList) {
        super(context, R.layout.row_user, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_user, parent, false);
        User user = userList.get(position);
        TextView email = view.findViewById(R.id.user_email);
        TextView name = view.findViewById(R.id.user_name);
        TextView level = view.findViewById(R.id.user_level);
        ImageView update = view.findViewById(R.id.img_user_update);
        ImageView delete = view.findViewById(R.id.img_user_delete);
        name.setText(user.getName());
        email.setText(user.getEmail());
        if (user.isAdmin()) {
        level.setText("Admin");
        } else {
            level.setText("User");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });
        return view;
    }
}
