package com.jin.catsns;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jin.catsns.chat.ChatFragment;
import com.jin.catsns.knowledge.KnowledgeFragment;
import com.jin.catsns.song.SongFragment;
import com.jin.catsns.post.PostFragment;

public class CustomFragmentPageAdapter extends FragmentPagerAdapter {

    private static final String TAG = CustomFragmentPageAdapter.class.getSimpleName();

    private static final int FRAGMENT_COUNT = 4;

    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PostFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new KnowledgeFragment();
            case 3:
                return new SongFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "POST";
            case 1:
                return "MESSAGE";
            case 2:
                return "KNOWLEDGE";
            case 3:
                return "MAP";
        }
        return null;
    }
}
