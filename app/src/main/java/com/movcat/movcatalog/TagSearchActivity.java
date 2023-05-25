package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.HomeAdapter;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.databinding.ActivityTagSearchBinding;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.GameComment;

import java.util.ArrayList;

public class TagSearchActivity extends AppCompatActivity {
    private ActivityTagSearchBinding binding;
    private String selectedTag;
    private ArrayList<Game> gamesList;
    private ArrayList<Game> backupList;
    private HomeAdapter gamesAdapter;
    private RecyclerView.LayoutManager gamesLM;
    private FirebaseDatabase database;
    private DatabaseReference refGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTagSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        selectedTag = getIntent().getStringExtra(Constants.tagKey);

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refGames = database.getReference("games");

        gamesList = new ArrayList<>();
        backupList = new ArrayList<>();
        gamesAdapter = new HomeAdapter(gamesList, R.layout.game_view_holder, this);
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        gamesLM = new GridLayoutManager(TagSearchActivity.this, columnas);
        binding.contentTag.resultsContainer.setAdapter(gamesAdapter);
        binding.contentTag.resultsContainer.setLayoutManager(gamesLM);

        prepareFirebaseListeners();
    }

    private void prepareFirebaseListeners(){
        refGames.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gamesList.clear();
                backupList.clear();
                if (snapshot.exists()) {
                    for ( DataSnapshot gameSnapshot : snapshot.getChildren() ) {
                        Game game = gameSnapshot.getValue(Game.class);
                        backupList.add(game);
                    }
                    gamesList.addAll(sortByTag());
                }
                gamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<Game> sortByTag(){
        ArrayList<Game> resultList = new ArrayList<>();
        for ( Game g : backupList ) {
            for ( String tag : g.getGenres() ) {
                if (tag.equals(selectedTag)) {
                    resultList.add(g);
                }
            }
        }
        return resultList;
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