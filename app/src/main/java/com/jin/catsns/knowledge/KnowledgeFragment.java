package com.jin.catsns.knowledge;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jin.catsns.R;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeFragment extends Fragment {


    public KnowledgeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge, container, false);

        RecyclerView playlisRecyclerView = (RecyclerView)view.findViewById(R.id.your_play_list);
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
        playlisRecyclerView.setLayoutManager(gridLayout);
        playlisRecyclerView.setHasFixedSize(true);

        KnowledgeAdapter mAdapter = new KnowledgeAdapter(getActivity(), getTestData());
        playlisRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public List<KnowledgeObject> getTestData() {
        List<KnowledgeObject> trackList = new ArrayList<KnowledgeObject>();
        trackList.add(new KnowledgeObject("행동언어", "고양이의 행동언어", "ㅎㅎ"));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        trackList.add(new KnowledgeObject("Falling over", "12 tracks", ""));
        return trackList;
    }
}
