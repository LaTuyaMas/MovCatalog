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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.GameViewAdapter;
import com.movcat.movcatalog.adapters.TagsAdapter;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.databinding.ActivityGameViewBinding;
import com.movcat.movcatalog.models.Date;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.GameComment;
import com.movcat.movcatalog.models.User;
import com.movcat.movcatalog.models.UserComment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;

public class GameViewActivity extends AppCompatActivity {
    private ActivityGameViewBinding binding;
    private int score;
    private Game viewGame;
    private User currentUser;
    private NumberFormat priceFormat;
    private List<GameComment> commentsList;
    private GameViewAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLM;
    private List<String> tagsList;
    private TagsAdapter tagsAdapter;
    private RecyclerView.LayoutManager tagsLM;
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
        tagsList = new ArrayList<>();
        priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setCurrency(Currency.getInstance("EUR")); // Set currency symbol (e.g., USD)
        priceFormat.setMaximumFractionDigits(2);

        if (gameId != null) {
            database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
            refGame = database.getReference("games").child(gameId);
            refUser = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            commentsAdapter = new GameViewAdapter(commentsList, R.layout.comment_view_holder, this);
            int columnas;
            columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
            commentsLM = new GridLayoutManager(GameViewActivity.this, columnas);
            prepareFirebaseListeners();
            prepareComponentsListeners();
            binding.contentGame.commentsContainer.setAdapter(commentsAdapter);
            binding.contentGame.commentsContainer.setLayoutManager(commentsLM);

            tagsAdapter = new TagsAdapter(tagsList, R.layout.tag_button_view_holder, this);
            tagsLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.contentGame.tagsContainer.setAdapter(tagsAdapter);
            binding.contentGame.tagsContainer.setLayoutManager(tagsLM);
        }
    }

    private void prepareFirebaseListeners(){
        refGame.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewGame = null;
                commentsList.clear();
                tagsList.clear();
                if (snapshot.exists()) {
                    viewGame = snapshot.getValue(Game.class);
                    if (viewGame != null && viewGame.getComments() != null) {
                        commentsList.addAll(viewGame.getComments());
                        tagsList.addAll(viewGame.getGenres());
                    }
                    commentsAdapter.notifyDataSetChanged();
                    tagsAdapter.notifyDataSetChanged();
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
                currentUser = null;
                if (snapshot.exists()) {
                    currentUser = snapshot.getValue(User.class);
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
                            currentUser.getNickname(),
                            comment,
                            viewGame.getId(),
                            currentUser.getUser_uid()
                    );
                    UserComment ucomment = new UserComment(
                            viewGame.getId(),
                            viewGame.getName(),
                            comment,
                            date,
                            score
                    );
                    viewGame.addComment(gcomment);
                    refGame.setValue(viewGame);
                    currentUser.addComment(ucomment);
                    refUser.setValue(currentUser);
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

        binding.contentGame.lblDevelopersView.setText("Developers: ");
        for ( String d : viewGame.getDevelopers() ) {
            binding.contentGame.lblDevelopersView.append(d+" ");
        }

        binding.contentGame.lblPublishersView.setText("Publishers: ");
        for ( String p : viewGame.getPublishers() ) {
            binding.contentGame.lblPublishersView.append(p+" ");
        }

        binding.contentGame.lblDateView.setText(viewGame.getReleaseDate().getDay()+"/"+viewGame.getReleaseDate().getMonth()+"/"+viewGame.getReleaseDate().getYear());

        String formattedPrice = priceFormat.format(viewGame.getPrice());
        binding.contentGame.lblPriceView.setText(formattedPrice);

        binding.contentGame.lblScoreView.setText(String.valueOf(getAverageScore()));

        //Este arreglo esta totalmento hecho por ChatGPT porque no tenía ni idea como hacer
        //que no diera una excepción cuando necesito mirar los comentarios al actualizar la página
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Iterator<GameComment> iterator = commentsList.iterator();
        while (iterator.hasNext()) {
            GameComment comment = iterator.next();
            if (comment.getUserUid().equals(userUid)) {
                binding.contentGame.cardGameView.setVisibility(View.GONE);
                iterator.remove();
                commentsList.add(0, comment);
                break;
            }
        }
    }

    private int getAverageScore() {
        if (commentsList.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for ( GameComment c : commentsList ) {
            sum += c.getScore();
        }
        return sum / commentsList.size();
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