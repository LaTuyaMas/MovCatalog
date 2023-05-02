package com.movcat.movcatalog.models;

public class UserComment {
    private String game_uid;
    private String game_name;
    private String game_icon;
    private String comment;
    private Date date;

    public String getGame_uid() {return game_uid;}
    public void setGame_uid(String game_uid) {this.game_uid = game_uid;}

    public String getGame_name() {return game_name;}
    public void setGame_name(String game_name) {this.game_name = game_name;}

    public String getGame_icon() {return game_icon;}
    public void setGame_icon(String game_icon) {this.game_icon = game_icon;}

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}

    @Override
    public String toString() {
        return "UserComment{" +
                "game_uid='" + game_uid + '\'' +
                ", game_name='" + game_name + '\'' +
                ", game_icon='" + game_icon + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
