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
        trackList.add(new KnowledgeObject("사전", "고양이 사전", ""));
        trackList.add(new KnowledgeObject("행동언어", "고양이 행동언어", ""));
        trackList.add(new KnowledgeObject("마사지", "고양이 마사지", ""));
        trackList.add(new KnowledgeObject("먹으면안되는 음식", "고양이가 먹으면 안되는 음식", ""));
        trackList.add(new KnowledgeObject("건강", "고양이 건강", ""));
        trackList.add(new KnowledgeObject("사료", "고양이 사료", ""));
        return trackList;
    }
}
