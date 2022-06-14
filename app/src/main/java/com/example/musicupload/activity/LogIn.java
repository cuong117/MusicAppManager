package com.example.musicupload.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicupload.R;
import com.example.musicupload.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        fAuth = FirebaseAuth.getInstance();
        TextView email =(TextView) findViewById(R.id.email);
        TextView password =(TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = ProgressDialog.show(
                        LogIn.this, null, null, false, false
                );
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.getWindow().setContentView(R.layout.progress_bar);
                progressDialog.show();
                //check user
                if (isValid(email.getText().toString(), password.getText().toString())) {
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("user").child(fAuth.getCurrentUser().getUid());
                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user =snapshot.getValue(User.class);
                                                if (user.isAdmin() == true) {
                                                    startActivity(new Intent(LogIn.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(LogIn.this, "User is not admin!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                   } else {
                                       Toast.makeText(LogIn.this, "email or password is wrong!", Toast.LENGTH_SHORT).show();
                                   }
                                   if (progressDialog.isShowing()){
                                       progressDialog.dismiss();
                                   }
                               }
                           });

                } else {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(LogIn.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValid(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else return false;
    }
}