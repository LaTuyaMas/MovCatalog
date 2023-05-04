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
import com.movcat.movcatalog.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtEmailRegister.getText().toString();
                String password = binding.txtPasswordRegister.getText().toString();

                if (!email.isEmpty() && password.length() > 5){
                    doRegister(email, password);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Revisa los datos", Toast.LENGTH_SHORT).show();
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

    private void updateUI (FirebaseUser user) {
        if (user != null){
            String uid = user.getUid();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("UID", uid);
            startActivity(intent);
            finish();
        }
    }
}