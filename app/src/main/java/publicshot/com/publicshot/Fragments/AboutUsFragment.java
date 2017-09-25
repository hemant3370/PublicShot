package publicshot.com.publicshot.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import publicshot.com.publicshot.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        String aboutus = "<p align=”justify”><strong>About us</strong> <ul> <li><strong>Public shot</strong> is dedicated to the public Android app which any user can send such information with their location and date, which is necessary to know the society and the government from this app, the user can play the role of the fourth column of democracy.</li> <li>The purpose of the public shot is to work as a bridge between the common people and the government to help the administration know the facts and administration make the right decisions in the public interest. The work of public shots is beyond gender, religion and caste discrimination and language distinction.</li> <li>Public Shot will not allow any such information, video, audio to which an attempt has been made to offend the original sentiment of the nation and the Constitution.</li> <li>Public Shot is the medium supported by every user for the use of democratic rights. Public Shot will use all the legal streams made in the country to help the public.</li> </ul> <strong>Why do you need a public shot?</strong> <ul> <li>There are many newspapers in our country and a large number of electronic media but there are few people who run the information that gives information to such a large country. Most of the media houses are influenced by some thoughts or political parties.</li> <li>The journalist is not free in any media; one is either the editor's pressure or he has to think about the owner's advantage as the owner of the media is doing business, he has to make the government happy. In such a situation, \"public shots\" will help you to be honest and give you true information.</li> <li>Public Shot is giving this right to every person in the country that he can keep his thoughts in his own language without any newspaper, channel. This is the Public Shot Promise that your information will be delivered wherever you want to reach and it will also inform you. It is necessary that you use your democratic rights to live in the realm of the Constitution.</li> </ul> <strong>Disclaimer Warranties</strong> Although an effort is made to assure the accuracy and completeness of the information provided on thePublic Shot App, Website, Channel, social media sites and mobile applications. Public Shot,&amp; its affiliates take no express or implied warranty as to the accuracy, adequacy, completeness, legality, reliability or usefulness of the information. The Public Shot &amp; its Affiliates provides this information and all online services on an \"as is\" basis. While there may be changes to information on topics covered through online services, these changes may or may not be made available electronically. Applications assume no responsibility for anyone's use of the information or services";
        String privaciPolicy = "<strong>Privacy Statement</strong> This statement describes the information-gathering practices and procedures of Public Shot App &amp; website, social media sites, and mobile applications in general. However, our site and services contain many departmental and agency data, to provide comprehensive and convenient information and service resources for public, information concerning their own department or agency Web information- gathering practices, requirements, or policies. \"The newsletter contains information about [subject matter]. The information is not advice, and should not be treated as such.\" \"Without prejudice to the generality of the foregoing paragraph, we do not represent, warrant, undertake or guarantee of that the information in the newsletter is correct, accurate, complete or non- misleading.\" \"We will not be liable to you in respect of any special, indirect or consequential loss or damage.\" \"If a section of this disclaimer is determined by any court or other competent authority to be unlawful and/or unenforceable, the other sections of this disclaimer continue in effect.\" “We do not specifically undertake or mean to hurt any religious, political or moral sentiments of anybody.We absolutely undertake to follow Patriotism and prefer every matter of national interest and national development.” Also it is mandatory that no kind of monetary benefit or Royalty be exchanged from either or any party. The news that has been provided by the public is the sole responsibility of the reporter or the reporting authority. Public Shot and its affiliates will be in no way responsible for the reports displayed on th App or media. . .In all or any case Public Shot App &amp; its affiliates.is a platform itended for Fair use and bring about truth &amp; reality of facts. Public Shot &amp; its affiliates will not be responsible for invalid, concocted, or erroneous news given by any Party. Of course, every effort is being taken to bring out correct facts.. No legal liabilities would pass over to Public Shot &amp; its affiliates for any untoward complaints.</p>";
        WebView webView = (WebView) view.findViewById(R.id.about_wv);

        if (mParam1.equals("aboutus")) {
            webView.loadData(aboutus, "text/html", "UTF-8");
        }
        else{
            webView.loadData(privaciPolicy, "text/html", "UTF-8");
        }

        return view;
    }

}
