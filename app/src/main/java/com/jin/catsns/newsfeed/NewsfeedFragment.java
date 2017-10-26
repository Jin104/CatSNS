package com.jin.catsns.newsfeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jin.catsns.R;
import com.jin.catsns.message.SongAdapter;
import com.jin.catsns.message.SongObject;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedFragment extends Fragment {

    public NewsfeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        getActivity().setTitle("Cat");
        RecyclerView newsRecyclerView = (RecyclerView)view.findViewById(R.id.news_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setHasFixedSize(true);

        NewsfeedAdapter mAdapter = new NewsfeedAdapter(getActivity(), getTestData());
        newsRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public List<NewsfeedObject> getTestData() {
        List<NewsfeedObject> recentSongs = new ArrayList<NewsfeedObject>();
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        recentSongs.add(new NewsfeedObject("Adele", "Someone Like You", ""));
        return recentSongs;
    }
}
