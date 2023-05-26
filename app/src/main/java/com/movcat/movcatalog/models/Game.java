package com.movcat.movcatalog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
	private String id;
	private String name;
	private String icon;
	private String banner;
	private List<String> images;
	private float price;
	private List<String> developers;
	private List<String> publishers;
	private List<String> genres;
	private List<String> consoles;
	private Date releaseDate;
	private Date postDate;
	private List<GameComment> gameComments;

	public Game() {
		gameComments = new ArrayList<>();
	}

	public Game(String id, String name, String icon, String banner, List<String> images, float price, List<String> developers, List<String> publishers, List<String> genres, List<String> consoles, Date releaseDate, Date postDate, List<GameComment> gameComments) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.banner = banner;
		this.images = images;
		this.price = price;
		this.developers = developers;
		this.publishers = publishers;
		this.genres = genres;
		this.consoles = consoles;
		this.releaseDate = releaseDate;
		this.postDate = postDate;
		this.gameComments = gameComments;
	}

	public void setImages(List<String> images){
		this.images = images;
	}
	public List<String> getImages(){
		return images;
	}

	public void setComments(List<GameComment> GameComments){
		this.gameComments = GameComments;
	}
	public List<GameComment> getComments(){
		return gameComments;
	}

	public void setDevelopers(List<String> developers){
		this.developers = developers;
	}
	public List<String> getDevelopers(){
		return developers;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}
	public String getIcon(){
		return icon;
	}

	public void setBanner(String banner){
		this.banner = banner;
	}
	public String getBanner(){
		return banner;
	}

	public void setReleaseDate(Date releaseDate) {this.releaseDate = releaseDate;}
	public Date getReleaseDate() {return releaseDate;}

	public void setPostDate(Date postDate){
		this.postDate = postDate;
	}
	public Date getPostDate(){
		return postDate;
	}

	public void setPrice(float price){
		this.price = price;
	}
	public float getPrice(){
		return price;
	}

	public void setGenres(List<String> genres){
		this.genres = genres;
	}
	public List<String> getGenres(){
		return genres;
	}

	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}

	public void setPublishers(List<String> publishers){
		this.publishers = publishers;
	}
	public List<String> getPublishers(){
		return publishers;
	}

	public void setConsoles(List<String> consoles){
		this.consoles = consoles;
	}
	public List<String> getConsoles(){
		return consoles;
	}

	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}

	public void addComment(GameComment comment) { gameComments.add(0, comment); }

	@Override
 	public String toString(){
		return 
			"GameItem{" + 
			"images = '" + images + '\'' + 
			",comments = '" + gameComments + '\'' +
			",developers = '" + developers + '\'' + 
			",icon = '" + icon + '\'' + 
			",banner = '" + banner + '\'' + 
			",release_date = '" + releaseDate + '\'' + 
			",post_date = '" + postDate + '\'' + 
			",price = '" + price + '\'' + 
			",genres = '" + genres + '\'' + 
			",name = '" + name + '\'' + 
			",publishers = '" + publishers + '\'' + 
			",consoles = '" + consoles + '\'' +
			",_id = '" + id + '\'' +
			"}";
		}
}