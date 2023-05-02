package com.movcat.movcatalog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movcat.movcatalog.adapters.HomeAdapter;
import com.movcat.movcatalog.databinding.ActivityMainBinding;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.ResponseGames;
import com.movcat.movcatalog.services.ApiService;
import com.movcat.movcatalog.services.RetrofitObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ResponseGames responseGamesResponse;
    private ArrayList<Game> gamesList;
    private HomeAdapter homeAdapter;
    private RecyclerView.LayoutManager lm;
    private Retrofit retrofitObject;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar);

        gamesList = new ArrayList<>();

        homeAdapter = new HomeAdapter(gamesList, R.layout.game_view_holder, this);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.contentMain.contenedor.setAdapter(homeAdapter);
        binding.contentMain.contenedor.setLayoutManager(lm);

        retrofitObject = RetrofitObject.getConexion();
        apiService = retrofitObject.create(ApiService.class);

        initialLoadGames();
    }

    private void initialLoadGames() {
        Call<ResponseGames> initialLoad = apiService.getGames();

        initialLoad.enqueue(new Callback<ResponseGames>() {
            @Override
            public void onResponse(Call<ResponseGames> call, Response<ResponseGames> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    responseGamesResponse = response.body();
                    gamesList.addAll(responseGamesResponse.getGames());
                    homeAdapter.notifyItemRangeInserted(0, gamesList.size());
                }
            }

            @Override
            public void onFailure(Call<ResponseGames> call, Throwable t) {

            }
        });
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