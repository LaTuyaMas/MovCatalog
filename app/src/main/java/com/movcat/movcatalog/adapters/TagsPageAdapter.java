package com.movcat.movcatalog.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.movcat.movcatalog.R;
import com.movcat.movcatalog.TagSearchActivity;
import com.movcat.movcatalog.config.Constants;

import java.util.List;

public class TagsPageAdapter extends RecyclerView.Adapter<TagsPageAdapter.GameVH>{

    private final TagSearchActivity activity;
    private final List<String> objects;
    private final int resources;
    private final Context context;

    public TagsPageAdapter(TagSearchActivity activity, List<String> objects, int resources, Context context) {
        this.activity = activity;
        this.objects = objects;
        this.resources = resources;
        this.context = context;
    }

    @NonNull
    @Override
    public GameVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameVH(LayoutInflater.from(context).inflate(resources, null));
    }

    @Override
    public void onBindViewHolder(@NonNull GameVH holder, int position) {
        holder.cvTag.setCardBackgroundColor(Color.TRANSPARENT);
        String tag = objects.get(position);
        holder.btnTag.setText(tag);

        holder.btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.tagButtonPressed(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class GameVH extends RecyclerView.ViewHolder {
        CardView cvTag;
        AppCompatButton btnTag;

        public GameVH(@NonNull View itemView) {
            super(itemView);
            cvTag = itemView.findViewById(R.id.cvTag);
            btnTag = itemView.findViewById(R.id.btnTag);
        }
    }
}
