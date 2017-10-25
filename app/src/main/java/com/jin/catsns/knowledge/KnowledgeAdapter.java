package com.jin.catsns.knowledge;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jin.catsns.R;

import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeViewHolder>{

    private static final String TAG = KnowledgeAdapter.class.getSimpleName();

    private Context context;
    private List<KnowledgeObject> playlists;

    public KnowledgeAdapter(Context context, List<KnowledgeObject> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public KnowledgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.knowledge_layout, parent, false);
        return new KnowledgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KnowledgeViewHolder holder, int position) {
        KnowledgeObject playlistObject = playlists.get(position);
        holder.playlistTitle.setText(playlistObject.getPlaylistTitle());
        holder.playlistTracks.setText(playlistObject.getPlaylistTracks());

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
}
