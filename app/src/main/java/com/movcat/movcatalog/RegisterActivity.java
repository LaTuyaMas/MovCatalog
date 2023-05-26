package com.movcat.movcatalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.movcat.movcatalog.databinding.ActivityRegisterBinding;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.User;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference refUsers;
    private DatabaseReference refUser;

    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refUsers = database.getReference("users");

        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                if (snapshot.exists()) {
                    for ( DataSnapshot gameSnapshot : snapshot.getChildren() ) {
                        User user = gameSnapshot.getValue(User.class);
                        userList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean goodRegister = true;
                String nickname = binding.txtNicknameRegister.getText().toString();
                String email = binding.txtEmailRegister.getText().toString();
                String password = binding.txtPasswordRegister.getText().toString();

                for ( User u : userList ) {
                    if (u.getNickname().equals(nickname)){
                        goodRegister = false;
                        Toast.makeText(RegisterActivity.this, "That nickname is already used", Toast.LENGTH_SHORT).show();
                    }
                }

                if (password.length() < 5) {
                    Toast.makeText(RegisterActivity.this, "Password must be 6 characters long minimum", Toast.LENGTH_SHORT).show();
                }
                else if (!goodRegister) {
                    Toast.makeText(RegisterActivity.this, "Please revise email", Toast.LENGTH_SHORT).show();
                }
                else {
                    doRegister(email, password);
                }
            }
        });
    }

    private void doRegister(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            assert user != null;
                            saveUser(user);
                            updateUI(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUser (FirebaseUser user) {
        User newUser = new User();
        newUser.setUser_uid(user.getUid());
        newUser.setNickname(binding.txtNicknameRegister.getText().toString());
        newUser.setUserComments(null);
        refUser = database.getReference("users").child(user.getUid());
        refUser.setValue(newUser);
    }

    private void updateUI (FirebaseUser user) {
        if (user != null){
            String uid = user.getUid();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("UID", uid);
            startActivity(intent);
            finish();
        }
    }
}