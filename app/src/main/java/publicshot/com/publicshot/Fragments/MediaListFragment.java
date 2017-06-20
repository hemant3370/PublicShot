package publicshot.com.publicshot.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import publicshot.com.publicshot.Activities.ImageDetailActivity;
import publicshot.com.publicshot.Adapter.FeedAdapter;
import publicshot.com.publicshot.Applications.Initializer;
import publicshot.com.publicshot.Constants;
import publicshot.com.publicshot.CustomClickListener;
import publicshot.com.publicshot.Model.FeedItem;
import publicshot.com.publicshot.Model.FeedResponse;
import publicshot.com.publicshot.R;
import publicshot.com.publicshot.Retrofit.Client.RestClient;
import publicshot.com.publicshot.Retrofit.Loader.RetrofitLoader;
import publicshot.com.publicshot.Retrofit.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class MediaListFragment extends Fragment {
    @Bind(R.id.feed_recy)  RecyclerView mRecyclerView;
        private Adapter mAdapter;
        public CustomClickListener listener;
        private String type;
        @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

        private List<FeedItem> items = new ArrayList<>();
        @Inject
        Retrofit mRetrofit;
    LinearLayoutManager mLayoutManager;
    Call<FeedResponse> call;
    static final int PAGE_SIZE = 20 ;
    private OnScrollListener recyclerViewOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount > PAGE_SIZE) {
                    getFeeds();
                }
        }
    };

    public MediaListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MediaListFragment newInstance( String type) {
            MediaListFragment fragment = new MediaListFragment();
            Bundle args = new Bundle();
            args.putString("type", type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            ButterKnife.bind(this,rootView);
            ((Initializer) getActivity().getApplication()).getNetComponent().inject(this);

            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
            type = getArguments().getString("type");
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    items.clear();
                    getFeeds();
                }
            });
            listener = new CustomClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    FeedItem item = items.get(position);
                            Intent mIntent = new Intent(getActivity(), ImageDetailActivity.class);
                            mIntent.putExtra("item", item);
                            Pair<View, String> p1 = Pair.create(v.findViewById(R.id.feedIV), "media");
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(), p1);
                            startActivity(mIntent, options.toBundle());
                }
            };
                mAdapter = new FeedAdapter(getContext(), items, listener);
                getFeeds();
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }

        private void getFeeds() {
            if (mRetrofit == null){
                mRetrofit = RestClient.getClient();
            }
            int page = items.size() / PAGE_SIZE;
            ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
            int loaderID = 0;
            switch (type) {
                case "news":
                    loaderID = 1111;
                    call = apiInterface.getFeed(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), type, Integer.toString(page));
                    break;
                case "uploads":
                    loaderID = 2222;
                    call = apiInterface.myUploads(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), Integer.toString(page));
                    break;
                case "audio": case"video": case "image":
                    loaderID = type.hashCode();
                    call = apiInterface.get(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), type, Integer.toString(page));
                    break;
                 default:
                     loaderID = type.hashCode();
                     call = apiInterface.search(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""),type,Integer.toString(page));
                     break;
            }
//            call.enqueue(new Callback<FeedResponse>() {
//                @Override
//                public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
//                    if (response.code() == 200 ){
//                        items.addAll(response.body().getData());
//                        mAdapter.notifyDataSetChanged();
//                        swipeContainer.setRefreshing(false);
//                    }
//                    else {
//                        swipeContainer.setRefreshing(false);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<FeedResponse> call, Throwable t) {
//                    swipeContainer.setRefreshing(false);
//                }
//            });
            RetrofitLoader.load(mRecyclerView.getContext(), getActivity().getLoaderManager(), loaderID, call, new Callback<FeedResponse>() {
                @Override
                public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                    if (response.code() == 200 ){
                                items.addAll(response.body().getData());
                                mAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                    else if (response.code() == 403){
                        Toast.makeText(getContext(),"Please Login again",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Error " + response.message(),Toast.LENGTH_SHORT).show();
                        if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                    }
                }

                @Override
                public void onFailure(Call<FeedResponse> call, Throwable t) {
                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                }
            });

        }

    private void removeListeners(){
        mRecyclerView.removeOnScrollListener(recyclerViewOnScrollListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        call.cancel();
        removeListeners();
        ButterKnife.unbind(this);
    }
}
