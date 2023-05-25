package com.movcat.movcatalog.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.movcat.movcatalog.R;
import com.movcat.movcatalog.TagSearchActivity;
import com.movcat.movcatalog.config.Constants;
import com.movcat.movcatalog.models.GameComment;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.GameVH>{

    private final List<String> objects;
    private final int resources;
    private final Context context;

    public TagsAdapter(List<String> objects, int resources, Context context) {
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
        String tag = objects.get(position);
        holder.btnTag.setText(tag);

        holder.btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TagSearchActivity.class);
                intent.putExtra(Constants.tagKey, tag);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class GameVH extends RecyclerView.ViewHolder {
        AppCompatButton btnTag;

        public GameVH(@NonNull View itemView) {
            super(itemView);

            btnTag = itemView.findViewById(R.id.btnTag);
        }
    }
}
