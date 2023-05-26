package com.movcat.movcatalog.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.movcat.movcatalog.models.UserComment;

import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserVH>{

    private final List<UserComment> objects;
    private final int resources;
    private final Context context;

    public UserCommentAdapter(List<UserComment> objects, int resources, Context context) {
        this.objects = objects;
        this.resources = resources;
        this.context = context;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVH(LayoutInflater.from(context).inflate(resources, null));
    }

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
                Intent intent = new Intent(context, GameViewActivity.class);
                intent.putExtra(Constants.gameKey, comment.getGame_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
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
