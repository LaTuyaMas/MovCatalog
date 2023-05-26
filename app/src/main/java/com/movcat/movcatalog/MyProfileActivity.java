package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.UserCommentAdapter;
import com.movcat.movcatalog.databinding.ActivityMyProfileBinding;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.User;
import com.movcat.movcatalog.models.UserComment;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
    private ActivityMyProfileBinding binding;
    private User currentUser;
    private List<UserComment> commentsList;
    private UserCommentAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLM;
    private FirebaseDatabase database;
    private DatabaseReference refUser;
    private DatabaseReference refGames;
    private List<Game> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refUser = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refGames = database.getReference("games");

        gamesList = new ArrayList<>();
        commentsList = new ArrayList<>();
        commentsAdapter = new UserCommentAdapter(commentsList, R.layout.user_comment_view_holder, this, gamesList);
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
        commentsLM = new GridLayoutManager(MyProfileActivity.this, columnas);
        prepareFirebaseListeners();
        prepareComponentsListeners();
        binding.contentProfile.userCommentsContainer.setAdapter(commentsAdapter);
        binding.contentProfile.userCommentsContainer.setLayoutManager(commentsLM);
    }

    private void prepareFirebaseListeners(){
        refUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = null;
                commentsList.clear();
                if (snapshot.exists()) {
                    currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        commentsList.addAll(currentUser.getUserComments());
                    }
                    if (currentUser != null) {
                        binding.contentProfile.txtNicknameProfile.setText(currentUser.getNickname());
                    }
                    commentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gamesList.clear();
                if (snapshot.exists()) {
                    for ( DataSnapshot gameSnapshot : snapshot.getChildren() ) {
                        Game game = gameSnapshot.getValue(Game.class);
                        gamesList.add(game);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void prepareComponentsListeners() {
        binding.contentProfile.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = binding.contentProfile.txtNicknameProfile.getText().toString();
                if (newName.isEmpty()) {
                    Toast.makeText(MyProfileActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    confirmNickname(newName).show();
                }
            }
        });

        binding.contentProfile.btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeletion().show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private AlertDialog confirmNickname(String newName) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfileActivity.this);

        builder.setCancelable(false);
        TextView mensaje = new TextView(MyProfileActivity.this);
        mensaje.setText("Are you sure you want to change your nickname?");
        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.BLACK);
        mensaje.setPadding(50,100,50,100);
        builder.setView(mensaje);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentUser.setNickname(newName);
                refUser.setValue(currentUser);
            }
        });
        return builder.create();
    }

    @SuppressLint("SetTextI18n")
    private AlertDialog confirmDeletion() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfileActivity.this);

        builder.setCancelable(false);
        TextView mensaje = new TextView(MyProfileActivity.this);
        mensaje.setText("ARE YOU SURE YOU WANT TO DELETE YOUR ACCOUNT?");
        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.RED);
        mensaje.setPadding(50,100,50,100);
        builder.setView(mensaje);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refUser.removeValue();
                startActivity(new Intent(MyProfileActivity.this, LoginActivity.class));
            }
        });
        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}