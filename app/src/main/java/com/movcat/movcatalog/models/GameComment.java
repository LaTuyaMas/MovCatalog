package com.movcat.movcatalog.models;

public class GameComment {
	private Date date;
	private int score;
	private String userName;
	private String comment;
	private String id;
	private String userUid;

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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
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
			",_id = '" + id + '\'' + 
			",user_uid = '" + userUid + '\'' + 
			"}";
		}
}
