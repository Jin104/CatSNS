package com.jin.catsns;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jin.catsns.knowledge.KnowledgeFragment;
import com.jin.catsns.message.SongFragment;

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
                return new SongFragment();
            case 1:
                return new SongFragment();
            case 2:
                return new KnowledgeFragment();
            case 3:
                return new SongFragment();
            /*case 1:
                return new KnowledgeFragment();
            case 2:
                return new AlbumFragment();
            case 3:
                return new ArtistFragment();*/
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
                return "NEWSFEED";
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
