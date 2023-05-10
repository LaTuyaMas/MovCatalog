package com.movcat.movcatalog.models;

public class GameComment {
	private Date date;
	private int score;
	private String userName;
	private String comment;
	private String gameId;
	private String userUid;

	public GameComment() {
	}

	public GameComment(Date date, int score, String userName, String comment, String id, String userUid) {
		this.date = date;
		this.score = score;
		this.userName = userName;
		this.comment = comment;
		this.gameId = id;
		this.userUid = userUid;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}

	public void setScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setGameId(String gameId){
		this.gameId = gameId;
	}

	public String getGameId(){
		return gameId;
	}

	public void setUserUid(String userUid){
		this.userUid = userUid;
	}

	public String getUserUid(){
		return userUid;
	}

	@Override
 	public String toString(){
		return 
			"CommentsItem{" + 
			"date = '" + date + '\'' + 
			",score = '" + score + '\'' + 
			",user_name = '" + userName + '\'' + 
			",comment = '" + comment + '\'' + 
			",_id = '" + gameId + '\'' +
			",user_uid = '" + userUid + '\'' + 
			"}";
		}
}
