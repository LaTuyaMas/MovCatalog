package com.movcat.movcatalog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movcat.movcatalog.R;
import com.movcat.movcatalog.models.GameComment;

import java.util.List;

public class GameViewAdapter extends RecyclerView.Adapter<GameViewAdapter.GameVH>{

    private final List<GameComment> objects;
    private final int resources;
    private final Context context;

    public GameViewAdapter(List<GameComment> objects, int resources, Context context) {
        this.objects = objects;
        this.resources = resources;
        this.context = context;
    }

    @NonNull
    @Override
    public GameVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameVH(LayoutInflater.from(context).inflate(resources, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GameVH holder, int position) {
        GameComment comment = objects.get(position);
        holder.lblScore.setText(String.valueOf(comment.getScore()));
        holder.lblComment.setText(comment.getComment());
        holder.lblUsername.setText(comment.getUserName());
        holder.lblDate.setText(comment.getDate().getDay()+"/"+comment.getDate().getMonth()+"/"+comment.getDate().getYear());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class GameVH extends RecyclerView.ViewHolder {
        TextView lblScore;
        TextView lblUsername;
        TextView lblComment;
        TextView lblDate;

        public GameVH(@NonNull View itemView) {
            super(itemView);

            lblScore = itemView.findViewById(R.id.lblScoreCommentView);
            lblUsername = itemView.findViewById(R.id.lblUsernameCommentView);
            lblComment = itemView.findViewById(R.id.lblCommentCommentView);
            lblDate = itemView.findViewById(R.id.lblDateCommentView);
        }
    }
}
