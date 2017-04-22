package hemant.com.publicshot.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import hemant.com.publicshot.Activities.ImageDetailActivity;
import hemant.com.publicshot.Adapter.FeedAdapter;
import hemant.com.publicshot.Applications.Initializer;
import hemant.com.publicshot.Constants;
import hemant.com.publicshot.CustomClickListener;
import hemant.com.publicshot.Model.FeedItem;
import hemant.com.publicshot.Model.FeedResponse;
import hemant.com.publicshot.R;
import hemant.com.publicshot.Retrofit.Client.RestClient;
import hemant.com.publicshot.Retrofit.Services.ApiInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class MediaListFragment extends Fragment {
    /**
     * A placeholder fragment containing a simple view.
     */
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_ITEMS = "items";
        @Bind(R.id.feed_recy)  RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        public CustomClickListener listener;
        private String type;
        @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    private List<FeedItem> items;
        @Inject
        Retrofit mRetrofit;
    private Realm realm;


    public MediaListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MediaListFragment newInstance( List<FeedItem> items, String type) {
            MediaListFragment fragment = new MediaListFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_ITEMS, (ArrayList<? extends Parcelable>) items);
            args.putString("type", type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            ButterKnife.bind(this,rootView);
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME)
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(realmConfiguration);
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
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            listener = new CustomClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    FeedItem item = items.get(position);
//                    switch (item.getType()) {
//                        case "image":
                            Intent mIntent = new Intent(getActivity(), ImageDetailActivity.class);
                            mIntent.putExtra("item", item);
                            Pair<View, String> p1 = Pair.create(v.findViewById(R.id.feedIV), "media");
//                            Pair<View, String> p2 = Pair.create(v.findViewById(R.id.feed_title), "title");
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(), p1);
                            startActivity(mIntent, options.toBundle());
//                            break;
//                        case "video":
//                            Intent video = new Intent(getActivity(), VideoFullscreenActivity.class);
//                            video.putExtra("video", item);
//                            startActivity(video);
//                            break;
//                        default:
//                            final JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) slideView.findViewById(R.id.videoplayer);
//                            jcVideoPlayerStandard.setUp(Constants.MyUrl.BASE_URL + item.getMediaUrl()
//                                    , JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, item.getName());
//                            jcVideoPlayerStandard.startVideo();
//                            View.OnClickListener listener = new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    JCVideoPlayerStandard.releaseAllVideos();
//                                    JCVideoPlayerStandard.backPress();
//                                    slideUp.hide();
//                                }
//                            };
//                            slideView.findViewById(R.id.close_audio).setOnClickListener(listener);
//                            jcVideoPlayerStandard.titleTextView.setOnClickListener(listener);
//                            jcVideoPlayerStandard.backButton.setOnClickListener(listener);
//                            jcVideoPlayerStandard.tinyBackImageView.setOnClickListener(listener);
//                            slideUp.show();
//                            break;
//                    }
                }
            };
            items = getArguments().getParcelableArrayList(ARG_ITEMS);
            type = getArguments().getString("type");
            mAdapter = new FeedAdapter(getContext(), items, listener);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }

        private void getFeeds() {

            if (mRetrofit == null){
                mRetrofit = RestClient.getClient();
            }
            ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
            Call<FeedResponse> call = apiInterface.getFeed(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey,""));
            call.enqueue(new retrofit2.Callback<FeedResponse>() {
                @Override
                public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {

                    if (response.code() == 200 ){
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(response.body().getData());
                        realm.commitTransaction();
                        if (!type.equals("")) {
                            if (type.equals("uploads")){
                                items =  realm.copyFromRealm(realm.where(FeedItem.class).equalTo("userId.id",
                                        Initializer.readFromPreferences(getContext(),Constants.SPKeys.userIDKey,"")).findAll().sort("createdAt", Sort.DESCENDING));
                                mAdapter = new FeedAdapter(getContext(),items, listener);
                            }
                            else {
                                items =  realm.copyFromRealm(realm.where(FeedItem.class).equalTo("type", type).findAll().sort("createdAt", Sort.DESCENDING));
                                mAdapter = new FeedAdapter(getContext(),items, listener);
                            }
                        }
                        else {
                            items =  realm.copyFromRealm(realm.where(FeedItem.class).findAll().sort("createdAt", Sort.DESCENDING));
                            mAdapter = new FeedAdapter(getContext(), items, listener);
                        }
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.invalidate();
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
}
