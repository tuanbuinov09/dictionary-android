package com.myapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.dictionary.fragment.EnWordDetailFragment;
import com.myapp.dictionary.fragment.ImageFragment;
import com.myapp.dictionary.fragment.YourNoteFragment;
import com.myapp.model.EnWord;

public class ViewPagerAdapterProdDetail extends FragmentPagerAdapter {
    public ViewPagerAdapterProdDetail(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            Bundle bundle = new Bundle();
//                bundle.putInt("enWordId", enWordId);
            ImageFragment imageFragment = new ImageFragment();
            imageFragment.setArguments(bundle);
            return imageFragment;
        }
        else if (position == 1)
        {
                Bundle bundle = new Bundle();
//                bundle.putInt("enWordId", enWordId);
                YourNoteFragment yourNoteFragment = new YourNoteFragment();
                yourNoteFragment.setArguments(bundle);
                return yourNoteFragment;
        }

        return fragment;
    }


    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Tổng quan";
        }
        else if (position == 1)
        {
            title = "Đánh giá";
        }
        return title;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
