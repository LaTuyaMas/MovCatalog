package com.movcat.movcatalog.models;

import java.util.List;

public class ResponseGames {
	private List<Game> game;

	public void setGames(List<Game> game){
		this.game = game;
	}

	public List<Game> getGames(){
		return game;
	}

	@Override
 	public String toString(){
		return 
			"Games{" +
			"game = '" + game + '\'' + 
			"}";
		}
}