package hemant.com.publicshot.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import georgianhemant.com.imagegallery.ImageTabsActivity;
import hemant.com.publicshot.Activities.VideoFullscreenActivity;
import hemant.com.publicshot.Adapter.FeedAdapter;
import hemant.com.publicshot.Applications.Initializer;
import hemant.com.publicshot.Constants;
import hemant.com.publicshot.CustomClickListener;
import hemant.com.publicshot.Enums.MediaType;
import hemant.com.publicshot.Model.FeedItem;
import hemant.com.publicshot.Model.FeedResponse;
import hemant.com.publicshot.R;
import hemant.com.publicshot.Retrofit.Client.RestClient;
import hemant.com.publicshot.Retrofit.Services.ApiInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFABInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Inject
    Retrofit mRetrofit;
    ApiInterface apiInterface;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PlaceholderFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    // TODO: Rename and change types of parameters
    private FeedResponse feed;

    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private OnFABInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(FeedResponse param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            feed = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((Initializer) getActivity().getApplication()).getNetComponent().inject(this);

        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionMenu fab = (FloatingActionMenu) rootView.findViewById(R.id.fab);
        floatingActionButton1 = (FloatingActionButton) rootView.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) rootView.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) rootView.findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                mListener.openUploadFragment(MediaType.VIDEO);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                mListener.openUploadFragment(MediaType.IMAGE);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                mListener.openUploadFragment(MediaType.AUDIO);
            }
        });
        mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getChildFragmentManager(),feed);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        return rootView;
    }

    private void getFeeds() {

        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        apiInterface = mRetrofit.create(ApiInterface.class);
        Call<FeedResponse> call = apiInterface.getFeed(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey,""));
        call.enqueue(new retrofit2.Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {

                if (response.code() == 200 ){
                    feed = response.body();
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
                else {

                    Toast.makeText(getActivity(),String.valueOf(response.raw().message()),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {

                Toast.makeText(getActivity(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFABInteractionListener) {
            mListener = (OnFABInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFABInteractionListener {
        // TODO: Update argument type and name
        void openUploadFragment(MediaType type);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_ITEMS = "items";
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private LinearLayoutManager mLayoutManager;
        public CustomClickListener listener;
        private View mProgressView;
        private SwipeRefreshLayout swipeContainer;
        List<FeedItem> items;
        @Inject
        Retrofit mRetrofit;

        ApiInterface apiInterface;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance( List<FeedItem> items, String section) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_ITEMS, (ArrayList<? extends Parcelable>) items);
            args.putString("Section", section);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            // Lookup the swipe container view
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getFeeds();
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recy);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            listener = new CustomClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    FeedItem item = items.get(position);
                    if (item.getType().equals("image")) {
                        Intent mIntent = new Intent(getActivity(), ImageTabsActivity.class);
                        mIntent.putExtra("files", new String[]{Constants.MyUrl.BASE_URL + item.getMediaUrl()}); //urls list or array
                        mIntent.putExtra("index", position);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(), v, "media");
                        startActivity(mIntent,options.toBundle());
                    }
                    else {
                        Intent video = new Intent(getActivity(),VideoFullscreenActivity.class);
                        video.putExtra("video",item);
                        startActivity(video);
                    }
                }
            };
            items = getArguments().getParcelableArrayList(ARG_ITEMS);
            mAdapter = new FeedAdapter(getContext(), items, listener);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }

        private void getFeeds() {

            if (mRetrofit == null){
                mRetrofit = RestClient.getClient();
            }
            apiInterface = mRetrofit.create(ApiInterface.class);
            Call<FeedResponse> call = apiInterface.getFeed(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey,""));
            call.enqueue(new retrofit2.Callback<FeedResponse>() {
                @Override
                public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {

                    if (response.code() == 200 ){
                        mAdapter = new FeedAdapter(getContext(),response.body().getData(),listener);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeContainer.setRefreshing(false);
                    }
                    else {
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(getActivity(),String.valueOf(response.raw().message()),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<FeedResponse> call, Throwable t) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getActivity(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
        /**
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
         * one of the sections/tabs/pages.
         */
        public static class SectionsPagerAdapter extends FragmentPagerAdapter {

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
                        return PlaceholderFragment.newInstance(items.getData(), getPageTitle(position).toString());
                    case 1:
//                        List<FeedItem> filteredvideoList= items.getData().stream().filter(article ->
//                                article.getType().equals("video")).collect(Collectors.toList());
                        return PlaceholderFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "video").findAll()), getPageTitle(position).toString());
                    case 2:
//                        List<FeedItem> filteredimageList= items.getData().stream().filter(article ->
//                                article.getType().equals("image")).collect(Collectors.toList());
                        return PlaceholderFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "image").findAll()), getPageTitle(position).toString());
                    case 3:
//                        List<FeedItem> filteredaudioList= items.getData().stream().filter(article ->
//                                article.getType().equals("audio")).collect(Collectors.toList());
                        return PlaceholderFragment.newInstance(realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", "audio").findAll()), getPageTitle(position).toString());
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

    }}
