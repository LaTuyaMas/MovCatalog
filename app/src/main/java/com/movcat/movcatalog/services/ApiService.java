package com.movcat.movcatalog.services;

import com.movcat.movcatalog.models.ResponseGames;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("games")
    Call<ResponseGames> getGames();
}
