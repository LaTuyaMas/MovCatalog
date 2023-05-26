package com.movcat.movcatalog.models;

public class UserComment {
    private String game_id;
    private String game_name;
    private String comment;
    private Date date;
    private int score;

    public UserComment() {
    }

    public UserComment(String game_id, String game_name, String comment, Date date, int score) {
        this.game_id = game_id;
        this.game_name = game_name;
        this.comment = comment;
        this.date = date;
        this.score = score;
    }

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}

    public String getGame_id() {return game_id;}
    public void setGame_id(String game_id) {this.game_id = game_id;}

    public int getScore() {return score;}
    public void setScore(int score) {this.score = score;}

    public String getGame_name() {return game_name;}
    public void setGame_name(String game_name) {this.game_name = game_name;}
}
