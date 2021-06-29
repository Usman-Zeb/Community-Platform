package com.example.community_stack.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.community_stack.FragmentDiscussion;
import com.example.community_stack.FragmentSuggestion;
import com.example.community_stack.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    String Topic;
    String suggestionID;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String Topic, String suggestionID) {
        super(fm);
        mContext = context;
        this.Topic=Topic;
        this.suggestionID=suggestionID;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("Topic",Topic);
        bundle.putString("suggestionID",suggestionID);

        switch(position){
            case 0:
                fragment = new FragmentSuggestion();
                break;
            case 1:
                fragment = new FragmentDiscussion();
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}