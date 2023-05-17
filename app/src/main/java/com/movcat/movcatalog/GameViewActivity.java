package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.databinding.ActivityGameViewBinding;
import com.movcat.movcatalog.models.Game;
import com.squareup.picasso.Picasso;

public class GameViewActivity extends AppCompatActivity {
    private ActivityGameViewBinding binding;
    private Game viewGame;
    private FirebaseDatabase database;
    private DatabaseReference refGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        String gameId = getIntent().getStringExtra(Constants.gameKey);

        if (gameId != null) {
            database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
            refGame = database.getReference("games").child(gameId);
            prepareFirebaseListeners();
        }
    }

    private void prepareFirebaseListeners(){
        refGame.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewGame = null;
                if (snapshot.exists()) {
                    viewGame = snapshot.getValue(Game.class);
                    updateInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateInfo() {
        binding.contentGame.lblNameView.setText(viewGame.getName());
        Picasso.get()
                .load(viewGame.getBanner())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.contentGame.imgBannerView);
        binding.contentGame.lblTagsView.setText("");
        for ( String t : viewGame.getGenres() ) {
            binding.contentGame.lblTagsView.append(t+"|");
        }
        binding.contentGame.lblDevelopersView.setText("Developers: ");
        for ( String d : viewGame.getDevelopers() ) {
            binding.contentGame.lblDevelopersView.append(d+" ");
        }
        binding.contentGame.lblPublishersView.setText("Publishers: ");
        for ( String p : viewGame.getPublishers() ) {
            binding.contentGame.lblPublishersView.append(p+" ");
        }
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