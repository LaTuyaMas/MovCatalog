package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.HomeAdapter;
import com.movcat.movcatalog.databinding.ActivityMainBinding;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.GameComment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Game> gamesList;
    private ArrayList<Game> backupList;
    private ArrayList<Game> recentList;
    private ArrayList<Game> highList;
    private HomeAdapter gamesAdapter;
    private HomeAdapter recentAdapter;
    private HomeAdapter highAdapter;
    private RecyclerView.LayoutManager gamesLM;
    private RecyclerView.LayoutManager recentLM;
    private RecyclerView.LayoutManager highLM;
    private FirebaseDatabase database;
    private DatabaseReference refGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refGames = database.getReference("games");

        gamesList = new ArrayList<>();
        backupList = new ArrayList<>();
        gamesAdapter = new HomeAdapter(gamesList, R.layout.game_view_holder, this);
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        gamesLM = new GridLayoutManager(MainActivity.this, columnas);
        binding.contentMain.allContainer.setAdapter(gamesAdapter);
        binding.contentMain.allContainer.setLayoutManager(gamesLM);

        recentList = new ArrayList<>();
        recentAdapter = new HomeAdapter(recentList, R.layout.game_view_holder, this);
        recentLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.contentMain.recentContainer.setAdapter(recentAdapter);
        binding.contentMain.recentContainer.setLayoutManager(recentLM);

        highList = new ArrayList<>();
        highAdapter = new HomeAdapter(highList, R.layout.game_view_holder, this);
        highLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.contentMain.highContainer.setAdapter(highAdapter);
        binding.contentMain.highContainer.setLayoutManager(highLM);

        prepareFirebaseListeners();
    }



    private void prepareFirebaseListeners(){
        refGames.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!backupList.isEmpty()) {
                    backupList.clear();
                }
                gamesList.clear();
                recentList.clear();
                highList.clear();
                if (snapshot.exists()) {
                    for ( DataSnapshot gameSnapshot : snapshot.getChildren() ) {
                        Game game = gameSnapshot.getValue(Game.class);
                        backupList.add(game);
                    }
                    ArrayList<Game> backup2 = new ArrayList<>(backupList);
                    gamesList.addAll(backupList);
                    recentList.addAll(sortByMostRecent(backupList));
                    highList.addAll(sortByScore(backup2));
                }
                gamesAdapter.notifyDataSetChanged();
                recentAdapter.notifyDataSetChanged();
                highAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<Game> sortByMostRecent(ArrayList<Game> gamesList){
        gamesList.sort(new Comparator<Game>() {
            @Override
            public int compare(Game obj1, Game obj2) {
                return obj1.getReleaseDate().compareTo(obj2.getReleaseDate());
            }
        });

        return gamesList;
    }

    private ArrayList<Game> sortByScore(ArrayList<Game> gamesList){
        gamesList.sort(new Comparator<Game>() {
            @Override
            public int compare(Game obj1, Game obj2) {
                double avgScore1 = getAverageScore(obj1.getComments());
                double avgScore2 = getAverageScore(obj2.getComments());

                return Double.compare(avgScore2, avgScore1);
            }
        });
        return gamesList;
    }

    private int getAverageScore(List<GameComment> commentList) {
        if (commentList.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for ( GameComment c : commentList ) {
            sum += c.getScore();
        }

        return sum / commentList.size();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Type back to return to normal");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterGameBySearch(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void filterGameBySearch(String query) {
        if (query.isEmpty() && backupList.size() != 0) {
            binding.contentMain.lblRecentHome.setVisibility(View.VISIBLE);
            binding.contentMain.recentContainer.setVisibility(View.VISIBLE);
            binding.contentMain.lblHighHome.setVisibility(View.VISIBLE);
            binding.contentMain.highContainer.setVisibility(View.VISIBLE);
            binding.contentMain.lblAllGamesHome.setText("THE ENTIRE LIST");

            gamesList.clear();
            gamesList.addAll(backupList);
            backupList.clear();
            gamesAdapter.notifyDataSetChanged();
        }
        else {
            binding.contentMain.lblRecentHome.setVisibility(View.GONE);
            binding.contentMain.recentContainer.setVisibility(View.GONE);
            binding.contentMain.lblHighHome.setVisibility(View.GONE);
            binding.contentMain.highContainer.setVisibility(View.GONE);
            binding.contentMain.lblAllGamesHome.setText("SEARCH RESULTS");

            if (backupList.size() == 0) {
                backupList.addAll(gamesList);
                gamesList.clear();
            }
            else {
                gamesList.clear();
                gamesList.addAll(backupList);
            }

            ArrayList<Game> filteredList = new ArrayList<>();
            for (Game g : gamesList) {
                if (g.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(g);
                }
            }

            gamesList.clear();
            gamesList.addAll(filteredList);
            gamesAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        if (id == R.id.tags){
            startActivity(new Intent(MainActivity.this, TagSearchActivity.class));
        }

        if (id == R.id.myprofile){
            startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}