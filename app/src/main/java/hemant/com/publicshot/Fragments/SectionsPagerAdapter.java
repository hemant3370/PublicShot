package hemant.com.publicshot.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hemant.com.publicshot.Model.FeedItem;
import hemant.com.publicshot.Model.FeedResponse;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
 public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Realm realm;
    FeedResponse items;



    public SectionsPagerAdapter(FragmentManager fm, FeedResponse items) {
        super(fm);
        this.items = items;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfiguration);

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return MediaListFragment.newInstance(items.getData(), "");
            case 1:
//                        List<FeedItem> filteredvideoList= items.getData().stream().filter(article ->
//                                article.getType().equals("video")).collect(Collectors.toList());
                return MediaListFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "video").findAll()), "video");
            case 2:
//                        List<FeedItem> filteredimageList= items.getData().stream().filter(article ->
//                                article.getType().equals("image")).collect(Collectors.toList());
                return MediaListFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "image").findAll()), "image");
            case 3:
//                        List<FeedItem> filteredaudioList= items.getData().stream().filter(article ->
//                                article.getType().equals("audio")).collect(Collectors.toList());
                return MediaListFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "audio").findAll()), "audio");
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
                return "ALL";
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
