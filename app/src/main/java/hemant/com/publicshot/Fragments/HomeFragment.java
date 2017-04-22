package hemant.com.publicshot.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;

import hemant.com.publicshot.Applications.Initializer;
import hemant.com.publicshot.Constants;
import hemant.com.publicshot.Enums.MediaType;
import hemant.com.publicshot.Model.FeedResponse;
import hemant.com.publicshot.R;
import hemant.com.publicshot.Retrofit.Client.RestClient;
import hemant.com.publicshot.Retrofit.Services.ApiInterface;
import hemant.com.publicshot.Transformer.ForegroundToBackgroundTransformer;
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
    private SectionsPagerAdapter mSectionsPagerAdapter;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),feed);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true,new ForegroundToBackgroundTransformer());
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

}
