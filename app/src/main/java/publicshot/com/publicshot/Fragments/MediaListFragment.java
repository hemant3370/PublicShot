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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import publicshot.com.publicshot.Activities.ImageDetailActivity;
import publicshot.com.publicshot.Adapter.FeedAdapter;
import publicshot.com.publicshot.Applications.Initializer;
import publicshot.com.publicshot.Constants;
import publicshot.com.publicshot.CustomClickListener;
import publicshot.com.publicshot.Model.FeedItem;
import publicshot.com.publicshot.Model.FeedResponse;
import publicshot.com.publicshot.R;
import publicshot.com.publicshot.Retrofit.Client.RestClient;
import publicshot.com.publicshot.Retrofit.Services.ApiInterface;
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
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
        private List<FeedItem> items = new ArrayList<>();
        @Inject
        Retrofit mRetrofit;
    LinearLayoutManager mLayoutManager;

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
            switch (type) {
                case "news":

                apiInterface.getFeed(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), type, Integer.toString(page))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<FeedResponse>() {
                                @Override
                                public void onSubscribe(Disposable disposable) {
                                    compositeDisposable.add(disposable);
                                }

                                @Override
                                public void onNext(FeedResponse response) {
                                    if (response != null){
                                        items.addAll(response.getData());
                                        mAdapter.notifyDataSetChanged();
                                        swipeContainer.setRefreshing(false);
                                    }
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                    Toast.makeText(getContext(), throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onComplete() {
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                }
                            });

                    break;
                case "uploads":

                   apiInterface.myUploads(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), Integer.toString(page))
                           .subscribeOn(Schedulers.newThread())
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe(new Observer<FeedResponse>() {
                               @Override
                               public void onSubscribe(Disposable disposable) {
                                   compositeDisposable.add(disposable);
                               }

                               @Override
                               public void onNext(FeedResponse response) {
                                   if (response != null){
                                       items.addAll(response.getData());
                                       mAdapter.notifyDataSetChanged();
                                       swipeContainer.setRefreshing(false);
                                   }
                                   if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                   Toast.makeText(getContext(), throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onComplete() {
                                   if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                               }
                           });

                    break;
                case "audio": case"video": case "image":

                    apiInterface.get(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""), type, Integer.toString(page))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<FeedResponse>() {
                                @Override
                                public void onSubscribe(Disposable disposable) {
                                    compositeDisposable.add(disposable);
                                }

                                @Override
                                public void onNext(FeedResponse response) {
                                    if (response != null){
                                        items.addAll(response.getData());
                                        mAdapter.notifyDataSetChanged();
                                        swipeContainer.setRefreshing(false);
                                    }
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                    Toast.makeText(getContext(), throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onComplete() {
                                    if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                }
                            });

                    break;
                 default:

                     apiInterface.search(Initializer.readFromPreferences(getContext(), Constants.SPKeys.authTokenKey, ""),type,Integer.toString(page))
                             .subscribeOn(Schedulers.newThread())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribe(new Observer<FeedResponse>() {
                                 @Override
                                 public void onSubscribe(Disposable disposable) {
                                     compositeDisposable.add(disposable);
                                 }

                                 @Override
                                 public void onNext(FeedResponse response) {
                                     if (response != null){
                                         items.addAll(response.getData());
                                         mAdapter.notifyDataSetChanged();
                                         swipeContainer.setRefreshing(false);
                                     }
                                     if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                 }

                                 @Override
                                 public void onError(Throwable throwable) {
                                     if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                     Toast.makeText(getContext(), throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                                 }

                                 @Override
                                 public void onComplete() {
                                     if (swipeContainer != null) { swipeContainer.setRefreshing(false); }
                                 }
                             });

                     break;
            }

        }

    private void removeListeners(){
        mRecyclerView.removeOnScrollListener(recyclerViewOnScrollListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
        removeListeners();
        ButterKnife.unbind(this);
    }
}
