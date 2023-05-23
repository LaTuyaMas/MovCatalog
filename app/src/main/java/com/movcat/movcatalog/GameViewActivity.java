package com.movcat.movcatalog;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.GameViewAdapter;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.databinding.ActivityGameViewBinding;
import com.movcat.movcatalog.models.Date;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.GameComment;
import com.movcat.movcatalog.models.TempUser;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameViewActivity extends AppCompatActivity {
    private ActivityGameViewBinding binding;
    private String username;
    private int score;
    private Game viewGame;
    private List<GameComment> commentsList;
    private GameViewAdapter adapter;
    private RecyclerView.LayoutManager lm;
    private FirebaseDatabase database;
    private DatabaseReference refGame;
    private DatabaseReference refUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        score = 5;
        String gameId = getIntent().getStringExtra(Constants.gameKey);
        commentsList = new ArrayList<>();

        if (gameId != null) {
            database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
            refGame = database.getReference("games").child(gameId);
            refUser = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            adapter = new GameViewAdapter(commentsList, R.layout.comment_view_holder, this);
            int columnas;
            columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
            lm = new GridLayoutManager(GameViewActivity.this, columnas);
            prepareFirebaseListeners();
            prepareComponentsListeners();
            binding.contentGame.commentsContainer.setAdapter(adapter);
            binding.contentGame.commentsContainer.setLayoutManager(lm);
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
                    if (viewGame != null && viewGame.getComments() != null) {
                        commentsList.addAll(viewGame.getComments());
                    }
                    adapter.notifyDataSetChanged();
                    updateInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = "";
                if (snapshot.exists()) {
                    TempUser user = snapshot.getValue(TempUser.class);
                    username = user.getNickname();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void prepareComponentsListeners() {
        binding.contentGame.sbScoreComment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int currentScore, boolean fromUser) {
                score = currentScore;
                binding.contentGame.lblScoreComment.setText(String.valueOf(score));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.contentGame.btnSendComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String comment = binding.contentGame.txtCommentComment.getText().toString();
                if (comment.isEmpty()) {
                    Toast.makeText(GameViewActivity.this, "You need to write a comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    LocalDate localdate = LocalDate.now();
                    Date date = new Date(localdate.getDayOfMonth(), localdate.getMonthValue(), localdate.getYear());
                    GameComment gcomment = new GameComment(
                            date,
                            score,
                            username,
                            comment,
                            viewGame.getId(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid()
                    );
                    viewGame.addComment(gcomment);
                    refGame.setValue(viewGame);
                }
            }
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
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

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for ( GameComment c : commentsList ) {
            if (c.getUserUid().equals(userUid)) {
                binding.contentGame.cardGameView.setVisibility(View.GONE);
                commentsList.remove(c);
                commentsList.add(0, c);
            }
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