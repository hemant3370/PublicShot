package publicshot.com.publicshot.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import publicshot.com.publicshot.Model.FeedResponse;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
 public class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm, FeedResponse items) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return MediaListFragment.newInstance( "news");
            case 1:
                return MediaListFragment.newInstance( "video");
            case 2:
                return MediaListFragment.newInstance( "image");
            case 3:
                return MediaListFragment.newInstance( "audio");
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "NEWS";
            case 1:
                return "VIDEOS";
            case 2:
                return "IMAGES";
            case 3:
                return "AUDIOS";
        }
        return null;
    }

}
