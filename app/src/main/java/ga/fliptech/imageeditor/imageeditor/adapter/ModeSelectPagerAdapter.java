package ga.fliptech.imageeditor.imageeditor.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ModeSelectPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();

    public ModeSelectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        mFragmentArrayList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentArrayList.size();
    }
}

