package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.HomeAdapter;
import com.movcat.movcatalog.adapters.TagsPageAdapter;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.databinding.ActivityTagSearchBinding;
import com.movcat.movcatalog.models.Game;

import java.util.ArrayList;

public class TagSearchActivity extends AppCompatActivity {
    private ActivityTagSearchBinding binding;
    private boolean fromGameView;
    private String selectedTag;
    private ArrayList<String> tagsList;
    private TagsPageAdapter tagsAdapter;
    private RecyclerView.LayoutManager tagsLM;
    private ArrayList<Game> gamesList;
    private ArrayList<Game> backupList;
    private HomeAdapter gamesAdapter;
    private RecyclerView.LayoutManager gamesLM;
    private FirebaseDatabase database;
    private DatabaseReference refGames;
    private DatabaseReference refTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTagSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refGames = database.getReference("games");
        refTags = database.getReference("tags");

        tagsList = new ArrayList<>();
        tagsAdapter = new TagsPageAdapter(this, tagsList, R.layout.tag_button_view_holder, this);
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5;
        tagsLM = new GridLayoutManager(this, columnas);
        binding.contentTag.tagsContainer.setAdapter(tagsAdapter);
        binding.contentTag.tagsContainer.setLayoutManager(tagsLM);

        gamesList = new ArrayList<>();
        backupList = new ArrayList<>();
        gamesAdapter = new HomeAdapter(gamesList, R.layout.game_view_holder, this);
        int columnas2;
        columnas2 = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        gamesLM = new GridLayoutManager(TagSearchActivity.this, columnas2);
        binding.contentTag.resultsContainer.setAdapter(gamesAdapter);
        binding.contentTag.resultsContainer.setLayoutManager(gamesLM);

        selectedTag = getIntent().getStringExtra(Constants.tagKey);
        if (selectedTag == null) {
            hideResults();
        }
        else {
            binding.contentTag.tagsContainer.setVisibility(View.GONE);
            fromGameView = true;
        }

        binding.contentTag.btnShowTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideResults();
                binding.contentTag.tagsContainer.setVisibility(View.VISIBLE);
            }
        });

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
                    if (fromGameView) {
                        prepareTagSearchResults();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refTags.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tagsList.clear();
                if (snapshot.exists()) {
                    for ( DataSnapshot tagSnapshot : snapshot.getChildren() ) {
                        String tag = tagSnapshot.getValue(String.class);
                        tagsList.add(tag);
                    }
                    tagsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hideResults(){
        fromGameView = false;
        binding.contentTag.lblTitleTag.setVisibility(View.GONE);
        binding.contentTag.resultsContainer.setVisibility(View.GONE);
        binding.contentTag.btnShowTag.setVisibility(View.GONE);
        gamesList.clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void prepareTagSearchResults(){
        gamesList.addAll(sortByTag());
        gamesAdapter.notifyDataSetChanged();
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

    public void tagButtonPressed(String tag){
        binding.contentTag.tagsContainer.setVisibility(View.GONE);
        binding.contentTag.lblTitleTag.setVisibility(View.VISIBLE);
        binding.contentTag.resultsContainer.setVisibility(View.VISIBLE);
        binding.contentTag.btnShowTag.setVisibility(View.VISIBLE);
        selectedTag = tag;
        prepareTagSearchResults();
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