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
import com.movcat.movcatalog.models.Date;
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
//        testGame3();
    }

    private void testGame() {
        Game game = new Game();
        game.setId("14467805GH");
        game.setName("Spelunky 2");
        game.setIcon("https://cdn2.steamgriddb.com/file/sgdb-cdn/icon/ba2fd310dcaa8781a9a652a31baf3c68.ico");
        game.setBanner("https://cdn.fanbyte.com/wp-content/uploads/2020/09/Spelunky-2-Key-Art.jpg?x25640");
        ArrayList<String> images = new ArrayList<>();
        images.add("https://cdn.cloudflare.steamstatic.com/steam/apps/418530/ss_69755bc2679253aa132928f261bdd059f215d342.1920x1080.jpg?t=1663719294");
        images.add("https://cdn.vox-cdn.com/thumbor/9XKj80fncbd2OFrdjN5P5EPLwDU=/0x0:2560x1440/1200x675/filters:focal(1076x516:1484x924)/cdn.vox-cdn.com/uploads/chorus_image/image/67401958/20200911130108_1.0.jpg");
        images.add("https://s3.amazonaws.com/prod-media.gameinformer.com/styles/full/s3/2020/09/11/2945e4c8/s2-3.jpg");
        game.setImages(images);
        game.setPrice((float) 19.99);
        ArrayList<String> dev = new ArrayList<>();
        dev.add("Mossmouth");
        dev.add("BlitWorks");
        game.setDevelopers(dev);
        ArrayList<String> pub = new ArrayList<>();
        pub.add("Mossmouth");
        game.setPublishers(pub);
        ArrayList<String> gen = new ArrayList<>();
        gen.add("Platformer");
        gen.add("Rogue-Like");
        gen.add("Adventure");
        gen.add("Multiplayer");
        game.setGenres(gen);
        ArrayList<String> con = new ArrayList<>();
        con.add("PC");
        con.add("Nintendo Switch");
        con.add("Xbox");
        con.add("PS4");
        game.setConsoles(con);
        Date release = new Date(15,9,2020);
        game.setReleaseDate(release);
        Date post = new Date(5,4,2023);
        game.setPostDate(post);
        ArrayList<GameComment> comments = new ArrayList<>();
        comments.add(new GameComment(
                post,
                9,
                "pepo",
                "good game",
                "14467805GH",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game.setComments(comments);
        gamesList.add(0, game);

        Game game2 = new Game();
        game2.setId("94642681OP");
        game2.setName("Magicka");
        game2.setIcon("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/46b63d3c-ae67-464c-9a37-670829b2a157/da34sy2-9b0227a9-0947-4f01-ad4e-8dc4382520bb.png/v1/fill/w_512,h_512/magicka___icon_by_blagoicons_da34sy2-fullview.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NTEyIiwicGF0aCI6IlwvZlwvNDZiNjNkM2MtYWU2Ny00NjRjLTlhMzctNjcwODI5YjJhMTU3XC9kYTM0c3kyLTliMDIyN2E5LTA5NDctNGYwMS1hZDRlLThkYzQzODI1MjBiYi5wbmciLCJ3aWR0aCI6Ijw9NTEyIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0._Jf5o2SZZpHamQXw7i51hIFZGqtr5jn7fcfHzknKco4");
        game2.setBanner("https://plitchcdn.azureedge.net/covers-en/Magicka_181.png?d=637345932790247458");
        ArrayList<String> images2 = new ArrayList<>();
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_514e6d7484e979f1879b59b4a1443d52bfde7cb1.1920x1080.jpg?t=1615973729");
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_e1d4017ccb5a3387b76807298dad84c719614765.1920x1080.jpg?t=1615973729");
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_a9250193739e4e8a4baeef3169cc416a975ae38c.1920x1080.jpg?t=1615973729");
        game2.setImages(images2);
        game2.setPrice((float) 9.99);
        ArrayList<String> dev2 = new ArrayList<>();
        dev2.add("Arrowhead Game Studios");
        game2.setDevelopers(dev2);
        ArrayList<String> pub2 = new ArrayList<>();
        pub2.add("Paradox Interactive");
        game2.setPublishers(pub2);
        ArrayList<String> gen2 = new ArrayList<>();
        gen2.add("Fantasy");
        gen2.add("Roleplay");
        gen2.add("Adventure");
        gen2.add("Multiplayer");
        game2.setGenres(gen2);
        ArrayList<String> con2 = new ArrayList<>();
        con2.add("PC");
        con2.add("Xbox");
        game2.setConsoles(con2);
        Date release2 = new Date(25,1,2011);
        game2.setReleaseDate(release2);
        Date post2 = new Date(5,4,2023);
        game2.setPostDate(post2);
        ArrayList<GameComment> comments2 = new ArrayList<>();
        comments2.add(new GameComment(
                post,
                9,
                "pepo",
                "good game",
                "94642681OP",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game2.setComments(comments2);
        gamesList.add(0, game2);

        Game game3 = new Game();
        game3.setId("12345678YU");
        game3.setName("Terraria");
        game3.setIcon("https://play-lh.googleusercontent.com/BoAvMI_6JGNRBp_3gFaVuLuqW_4J-rjtbR_giKFoJRvZmDiPtDlnLMur9cT7sTTfeos=w240-h480-rw");
        game3.setBanner("https://fs-prod-cdn.nintendo-europe.com/media/images/10_share_images/games_15/nintendo_switch_download_software_1/H2x1_NSwitchDS_Terraria.jpg");
        ArrayList<String> images3 = new ArrayList<>();
        images3.add("https://web54.pro/wp-content/uploads/2022/12/Obnovlenie-Terraria-145-priostanovleno-tak-kak-razrabotchiki-vzyali-mesyachnyj-pereryv.jpg");
        images3.add("https://cdn.mos.cms.futurecdn.net/ftCURjzcqaaxNdsfUVLiDj.jpg");
        images3.add("https://techraptor.net/sites/default/files/styles/image_header/public/2022-04/Terraria%201.4.3%20Update%20Console%20Mobile%20cover.jpg?itok=rMX8KENh");
        game3.setImages(images3);
        game3.setPrice((float) 9.99);
        ArrayList<String> dev3 = new ArrayList<>();
        dev3.add("Re-Logic");
        game3.setDevelopers(dev3);
        ArrayList<String> pub3 = new ArrayList<>();
        pub3.add("Re-Logic");
        game3.setPublishers(pub3);
        ArrayList<String> gen3 = new ArrayList<>();
        gen3.add("Survival");
        gen3.add("Sandbox");
        gen3.add("Action");
        gen3.add("Exploration");
        gen3.add("Multiplayer");
        game3.setGenres(gen3);
        ArrayList<String> con3 = new ArrayList<>();
        con3.add("PC");
        con3.add("Xbox");
        con3.add("PS4");
        con3.add("Nintendo Switch");
        con3.add("Android");
        con3.add("IOS");
        con3.add("PSVita");
        game3.setConsoles(con3);
        Date release3 = new Date(16,5,2011);
        game3.setReleaseDate(release3);
        Date post3 = new Date(5,4,2023);
        game3.setPostDate(post3);
        ArrayList<GameComment> comments3 = new ArrayList<>();
        comments3.add(new GameComment(
                post,
                9,
                "pepo",
                "good game",
                "12345678YU",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game3.setComments(comments3);
        gamesList.add(0, game3);

        refGames.setValue(gamesList);
    }
    private void testGame2(){
        Game game = new Game();
        game.setName("Spelunky 2");
        game.setIcon("https://cdn2.steamgriddb.com/file/sgdb-cdn/icon/ba2fd310dcaa8781a9a652a31baf3c68.ico");
        game.setBanner("https://cdn.fanbyte.com/wp-content/uploads/2020/09/Spelunky-2-Key-Art.jpg?x25640");
        ArrayList<String> images = new ArrayList<>();
        images.add("https://cdn.cloudflare.steamstatic.com/steam/apps/418530/ss_69755bc2679253aa132928f261bdd059f215d342.1920x1080.jpg?t=1663719294");
        images.add("https://cdn.vox-cdn.com/thumbor/9XKj80fncbd2OFrdjN5P5EPLwDU=/0x0:2560x1440/1200x675/filters:focal(1076x516:1484x924)/cdn.vox-cdn.com/uploads/chorus_image/image/67401958/20200911130108_1.0.jpg");
        images.add("https://s3.amazonaws.com/prod-media.gameinformer.com/styles/full/s3/2020/09/11/2945e4c8/s2-3.jpg");
        game.setImages(images);
        game.setPrice((float) 19.99);
        ArrayList<String> dev = new ArrayList<>();
        dev.add("Mossmouth");
        dev.add("BlitWorks");
        game.setDevelopers(dev);
        ArrayList<String> pub = new ArrayList<>();
        pub.add("Mossmouth");
        game.setPublishers(pub);
        ArrayList<String> gen = new ArrayList<>();
        gen.add("Platformer");
        gen.add("Rogue-Like");
        gen.add("Adventure");
        gen.add("Multiplayer");
        game.setGenres(gen);
        ArrayList<String> con = new ArrayList<>();
        con.add("PC");
        con.add("Nintendo Switch");
        con.add("Xbox");
        con.add("PS4");
        game.setConsoles(con);
        Date release = new Date(15,9,2020);
        game.setReleaseDate(release);
        Date post = new Date(5,4,2023);
        game.setPostDate(post);
        ArrayList<GameComment> comments = new ArrayList<>();
        comments.add(new GameComment(
                post,
                9,
                "pepo",
                "good game",
                "",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game.setComments(comments);
        String gameId = refGames.push().getKey();
        game.setId(gameId);
        refGames.child(gameId).setValue(game);
    }
    private void testGame3(){
        Game game2 = new Game();
        game2.setId("94642681OP");
        game2.setName("Magicka");
        game2.setIcon("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/46b63d3c-ae67-464c-9a37-670829b2a157/da34sy2-9b0227a9-0947-4f01-ad4e-8dc4382520bb.png/v1/fill/w_512,h_512/magicka___icon_by_blagoicons_da34sy2-fullview.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NTEyIiwicGF0aCI6IlwvZlwvNDZiNjNkM2MtYWU2Ny00NjRjLTlhMzctNjcwODI5YjJhMTU3XC9kYTM0c3kyLTliMDIyN2E5LTA5NDctNGYwMS1hZDRlLThkYzQzODI1MjBiYi5wbmciLCJ3aWR0aCI6Ijw9NTEyIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0._Jf5o2SZZpHamQXw7i51hIFZGqtr5jn7fcfHzknKco4");
        game2.setBanner("https://plitchcdn.azureedge.net/covers-en/Magicka_181.png?d=637345932790247458");
        ArrayList<String> images2 = new ArrayList<>();
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_514e6d7484e979f1879b59b4a1443d52bfde7cb1.1920x1080.jpg?t=1615973729");
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_e1d4017ccb5a3387b76807298dad84c719614765.1920x1080.jpg?t=1615973729");
        images2.add("https://cdn.cloudflare.steamstatic.com/steam/apps/42910/ss_a9250193739e4e8a4baeef3169cc416a975ae38c.1920x1080.jpg?t=1615973729");
        game2.setImages(images2);
        game2.setPrice((float) 9.99);
        ArrayList<String> dev2 = new ArrayList<>();
        dev2.add("Arrowhead Game Studios");
        game2.setDevelopers(dev2);
        ArrayList<String> pub2 = new ArrayList<>();
        pub2.add("Paradox Interactive");
        game2.setPublishers(pub2);
        ArrayList<String> gen2 = new ArrayList<>();
        gen2.add("Fantasy");
        gen2.add("Roleplay");
        gen2.add("Adventure");
        gen2.add("Multiplayer");
        game2.setGenres(gen2);
        ArrayList<String> con2 = new ArrayList<>();
        con2.add("PC");
        con2.add("Xbox");
        game2.setConsoles(con2);
        Date release2 = new Date(25,1,2011);
        game2.setReleaseDate(release2);
        Date post2 = new Date(5,4,2023);
        game2.setPostDate(post2);
        ArrayList<GameComment> comments2 = new ArrayList<>();
        comments2.add(new GameComment(
                post2,
                9,
                "pepo",
                "good game",
                "94642681OP",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game2.setComments(comments2);
        gamesList.add(0, game2);
        String gameId = refGames.push().getKey();
        game2.setId(gameId);
        refGames.child(gameId).setValue(game2);
    }
    private void testGame4(){
        Game game3 = new Game();
        game3.setName("Pizza Tower");
        game3.setIcon("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/73206bd9-257c-4f50-b1f3-59a306e24084/dfnrv7j-8aed9d86-e84d-4e0e-9c77-26d94f6f98df.png/v1/fill/w_512,h_512/pizza_tower_icon_ico_by_hatemtiger_dfnrv7j-fullview.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NTEyIiwicGF0aCI6IlwvZlwvNzMyMDZiZDktMjU3Yy00ZjUwLWIxZjMtNTlhMzA2ZTI0MDg0XC9kZm5ydjdqLThhZWQ5ZDg2LWU4NGQtNGUwZS05Yzc3LTI2ZDk0ZjZmOThkZi5wbmciLCJ3aWR0aCI6Ijw9NTEyIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.8DplPVGlf8F0bo5fm1O4ytbVp--adoWDWLi2xQTKnpc");
        game3.setBanner("https://cdn.cloudflare.steamstatic.com/steam/apps/2231450/header.jpg?t=1674756021");
        ArrayList<String> images3 = new ArrayList<>();
        images3.add("https://cdn.cloudflare.steamstatic.com/steam/apps/2231450/ss_3e70c43ffd6f492f6e4dce7965499d41fad47052.600x338.jpg?t=1674756021");
        images3.add("https://cdn.cloudflare.steamstatic.com/steam/apps/2231450/ss_367286326c9a6ba91d4b2f08d0ca5fd1a47f2455.116x65.jpg?t=1674756021");
        images3.add("https://cdn.cloudflare.steamstatic.com/steam/apps/2231450/ss_b1a38d0541d2428c9864e30071625cb5472c32da.116x65.jpg?t=1674756021");
        game3.setImages(images3);
        game3.setPrice((float) 19.50);
        ArrayList<String> dev3 = new ArrayList<>();
        dev3.add("Tour De Pizza");
        game3.setDevelopers(dev3);
        ArrayList<String> pub3 = new ArrayList<>();
        pub3.add("Tour De Pizza");
        game3.setPublishers(pub3);
        ArrayList<String> gen3 = new ArrayList<>();
        gen3.add("2D Platformer");
        gen3.add("Fast-Paced");
        gen3.add("Retro");
        gen3.add("Singleplayer");
        game3.setGenres(gen3);
        ArrayList<String> con3 = new ArrayList<>();
        con3.add("PC");
        game3.setConsoles(con3);
        Date release3 = new Date(26,1,2023);
        game3.setReleaseDate(release3);
        Date post3 = new Date(5,4,2023);
        game3.setPostDate(post3);
        ArrayList<GameComment> comments3 = new ArrayList<>();
        comments3.add(new GameComment(
                post3,
                9,
                "pepo",
                "good game",
                "12345678YU",
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ));
        game3.setComments(comments3);
        gamesList.add(0, game3);
        String gameId = refGames.push().getKey();
        game3.setId(gameId);
        refGames.child(gameId).setValue(game3);
    }

    private void prepareFirebaseListeners(){
        refGames.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gamesList.clear();
                recentList.clear();
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

        return super.onOptionsItemSelected(item);
    }
}