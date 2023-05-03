package com.movcat.movcatalog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.movcat.movcatalog.adapters.HomeAdapter;
import com.movcat.movcatalog.databinding.ActivityMainBinding;
import com.movcat.movcatalog.models.Date;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.ResponseGames;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ResponseGames responseGamesResponse;
    private ArrayList<Game> gamesList;
    private HomeAdapter homeAdapter;
    private RecyclerView.LayoutManager lm;
    private FirebaseDatabase database;
    private DatabaseReference refGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar);

        gamesList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://movcatalog-9d20f-default-rtdb.europe-west1.firebasedatabase.app/");
        refGames = database.getReference("games");

        homeAdapter = new HomeAdapter(gamesList, R.layout.game_view_holder, this);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        refGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gamesList.clear();
                if (snapshot.exists()) {
                    GenericTypeIndicator<ArrayList<Game>> gti = new GenericTypeIndicator<ArrayList<Game>>() {};
                    ArrayList<Game> temp = snapshot.getValue(gti);
                    gamesList.addAll(temp);
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.contentMain.contenedor.setAdapter(homeAdapter);
        binding.contentMain.contenedor.setLayoutManager(lm);

        //testGame();
    }

    private void testGame() {
        Game game = new Game();
        game.setId("10400305CD");
        game.setName("Test1");
        game.setIcon("http://cdn.onlinewebfonts.com/svg/img_558941.png");
        game.setBanner("https://upload.wikimedia.org/wikipedia/commons/f/f1/CheckersStandard.jpg");
        ArrayList<String> images = new ArrayList<>();
        images.add("https://t4.ftcdn.net/jpg/02/49/92/03/360_F_249920336_OAL1B6J8UvQGKIjZnThWrOLHRPUDG0pV.jpg");
        images.add("https://i.ebayimg.com/images/g/QFwAAOSwjARcZzM0/s-l500.jpg");
        images.add("http://atlas-content-cdn.pixelsquid.com/stock-images/checkers-pieces-red-D5QNmq3-600.jpg");
        game.setImages(images);
        game.setPrice((float) 19.99);
        ArrayList<String> dev = new ArrayList<>();
        dev.add("china probably");
        game.setDevelopers(dev);
        ArrayList<String> pub = new ArrayList<>();
        pub.add("everyone");
        game.setPublishers(pub);
        ArrayList<String> gen = new ArrayList<>();
        gen.add("Strategy");
        gen.add("Puzzle");
        game.setGenres(gen);
        ArrayList<String> con = new ArrayList<>();
        con.add("Home");
        con.add("Table");
        game.setConsoles(con);
        Date release = new Date(0,0,0);
        game.setReleaseDate(release);
        Date post = new Date(5,4,2023);
        gamesList.add(0, game);
        refGames.setValue(gamesList);
        /*
        * {"_id":{"$oid":"642d2aa8fdc4c191a3263952"},
        * "name":"Test1",
        * "icon":"http://cdn.onlinewebfonts.com/svg/img_558941.png",
        * "banner":"https://upload.wikimedia.org/wikipedia/commons/f/f1/CheckersStandard.jpg",
        * "images":["https://t4.ftcdn.net/jpg/02/49/92/03/360_F_249920336_OAL1B6J8UvQGKIjZnThWrOLHRPUDG0pV.jpg",
        * "https://i.ebayimg.com/images/g/QFwAAOSwjARcZzM0/s-l500.jpg",
        * "http://atlas-content-cdn.pixelsquid.com/stock-images/checkers-pieces-red-D5QNmq3-600.jpg"],
        * "price":{"$numberDouble":"19.99"},
        * "developers":["china probably"],
        * "publishers":["everyone"],
        * "genres":["Strategy","Puzzle"],
        * "consoles":["Home","Outside","Idk"],
        * "release_date":{"day":{"$numberInt":"0"},
        * "month":{"$numberInt":"0"},"year":{"$numberInt":"0"}},
        * "post_date":{"day":{"$numberInt":"5"},"month":{"$numberInt":"4"},"year":{"$numberInt":"2023"}},
        * "comments":[{"user_uid":null,"user_name":null,"comment":null,"score":null,"date":{"day":null,"month":null,"year":null},"_id":{"$oid":"642d2aa8fdc4c191a3263953"}}]}
        * */
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}