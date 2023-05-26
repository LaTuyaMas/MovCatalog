package com.movcat.movcatalog.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movcat.movcatalog.GameViewActivity;
import com.movcat.movcatalog.R;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.models.Game;
import com.movcat.movcatalog.models.GameComment;
import com.movcat.movcatalog.models.UserComment;

import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserVH>{

    private final List<UserComment> objects;
    private final int resources;
    private final Context context;
    private final List<Game> safeDelete;

    public UserCommentAdapter(List<UserComment> objects, int resources, Context context, List<Game> safeDelete) {
        this.objects = objects;
        this.resources = resources;
        this.context = context;
        this.safeDelete = safeDelete;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVH(LayoutInflater.from(context).inflate(resources, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        UserComment comment = objects.get(position);
        holder.lblScore.setText(String.valueOf(comment.getScore()));
        holder.lblGame.setText(comment.getGame_name());
        holder.lblDate.setText(comment.getDate().getDay()+"/"+comment.getDate().getMonth()+"/"+comment.getDate().getYear());
        holder.lblComment.setText(comment.getComment());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOrDelete(comment).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @SuppressLint("SetTextI18n")
    private AlertDialog goOrDelete(UserComment comment) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setCancelable(true);
        TextView mensaje = new TextView(context);
        mensaje.setText("DO YOU WANT TO GO TO THE ENTRY OR DELETE THE COMMENT?");
        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.BLACK);
        mensaje.setPadding(50,100,50,100);
        builder.setView(mensaje);

        builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmDeletion(comment).show();
            }
        });
        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, GameViewActivity.class);
                intent.putExtra(Constants.gameKey, comment.getGame_id());
                context.startActivity(intent);
            }
        });
        return builder.create();
    }

    @SuppressLint("SetTextI18n")
    private AlertDialog confirmDeletion(UserComment comment) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setCancelable(false);
        TextView mensaje = new TextView(context);
        mensaje.setText("ARE YOU SURE YOU WANT TO DELETE THIS COMMENT?");
        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.RED);
        mensaje.setPadding(50,100,50,100);
        builder.setView(mensaje);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for ( Game g : safeDelete ) {
                    if ( g.getComments() != null) {
                        for ( GameComment c : g.getComments() ) {
                            if (c.getGameId().equals(comment.getGame_id())){
                                safeDelete.remove(c);
                            }
                        }
                    }
                }
                objects.remove(comment);
                notifyDataSetChanged();
            }
        });
        return builder.create();
    }

    public static class UserVH extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView lblScore;
        TextView lblGame;
        TextView lblDate;
        TextView lblComment;

        public UserVH(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.llUserComment);
            lblScore = itemView.findViewById(R.id.lblScoreUserComment);
            lblGame = itemView.findViewById(R.id.lblGameUserComment);
            lblDate = itemView.findViewById(R.id.lblDateUserComment);
            lblComment = itemView.findViewById(R.id.lblCommentUserComment);
        }
    }
}
