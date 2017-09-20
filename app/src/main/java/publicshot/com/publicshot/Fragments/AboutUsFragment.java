package publicshot.com.publicshot.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        String aboutus = "Public shot are dedicated to the public Android app  which any user can send such information with\n" +
                "their location and date, which is necessary to know the society and the government  from this app, the\n" +
                "user can play the role of the fourth column of democracy\n" +
                "The purpose of the public shot is to work as a bridge between the common people and the government\n" +
                "to help the administration know the facts and administration make the right decisions in the public\n" +
                "interest. The work of public shots is beyond gender, religion and caste discrimination and language\n" +
                "distinction. Public Shot will not allow any such information, video, audio to which an attempt has been\n" +
                "made to offend the original sentiment of the nation and the Constitution. Public Shot is the medium\n" +
                "supported by every user for the use of democratic rights. Public Shot will use all the legal streams made\n" +
                "in the country to help the public.\n" +
                "Why do you need a public shot?\n" +
                "There are many newspapers in our country and a large number of electronic media but there are few\n" +
                "people who run the information that gives information to such a large country. Most of the media\n" +
                "houses are influenced by some thoughts or political parties.\n" +
                "The journalist is not free in any media; one is either the editor&#39;s pressure or he has to think about the\n" +
                "owner&#39;s advantage as the owner of the media is doing business, he has to make the government happy.\n" +
                "In such a situation, &quot;public shots&quot; will help you to be honest and give you true information. Public Shot is\n" +
                "giving this right to every person in the country that he can keep his thoughts in his own language\n" +
                "without any newspaper, channel. This is the Public Shot Promise that your information will be delivered\n" +
                "wherever you want to reach and it will also inform you. It is necessary that you use your democratic\n" +
                "rights to live in the realm of the Constitution.\n" +
                "\n" +
                "Disclaimer Warranties\n" +
                "Although an effort is made to assure the accuracy and completeness of the information provided on\n" +
                "thePublic Shot App, Website, Channel, social media sites and mobile applications.\n" +
                "Public Shot ,&amp; its affiliates takes no express or implied warranty as to the accuracy, adequacy,\n" +
                "completeness, legality, reliability or usefulness of the information. The Public Shot &amp; its Affiliates\n" +
                "provides this information and all online services on an &quot;as is&quot; basis. While there may be changes to\n" +
                "information on topics covered through online services, these changes may or may not be made available\n" +
                "electronically . Applications assume no responsibility for anyone&#39;s use of the itnformation or services\n";
        String privaciPolicy = "This statement describes the information-gathering practices and procedures of ..Public Shot App &amp;\n" +
                "website, social media sites, and mobile applications in general. However, our site and services contain\n" +
                "\n" +
                "many departmental and agency data, to provide comprehensive and convenient information and service\n" +
                "resources for public, information concerning their own department or agency Web information-\n" +
                "gathering practices, requirements, or policies.\n" +
                "&quot;The newsletter contains information about [subject matter].  The information is not advice, and should\n" +
                "not be treated as such.&quot;\n" +
                "&quot;Without prejudice to the generality of the foregoing paragraph, we do not represent, warrant,\n" +
                "undertake or guarantee of that the information in the newsletter is correct, accurate, complete or non-\n" +
                "misleading.&quot;\n" +
                "&quot;We will not be liable to you in respect of any special, indirect or consequential loss or damage.&quot;\n" +
                "&quot;If a section of this disclaimer is determined by any court or other competent authority to be unlawful\n" +
                "and/or unenforceable, the other sections of this disclaimer continue in effect.&quot;\n" +
                "\n" +
                "“We do not specifically undertake or mean to hurt any religious, political or moral sentiments of\n" +
                "anybody.We absolutely undertake to follow Patriotism and prefer every matter of national interest and\n" +
                "national development.”\n" +
                "Also it is mandatory that no kind of monetary benefit or Royalty be exchanged from either or any party.\n" +
                "The news that has been provided by the public is the sole responsibility of the reporter or the reporting\n" +
                "authority.\n" +
                "Public Shot and its affiliates will be in no way responsible for the reports displayed on th App or media. .\n" +
                ".In all or any case Public Shot App &amp; its affiliates.is a platform itended for Fair use and bring about truth\n" +
                "&amp; reality of facts.Public Shot &amp; its affiliates will not be responsible for invalid,concocted,or erroneous\n" +
                "news given by any Party. Of course,every effort is being taken to bring out correct facts.. No legal\n" +
                "liabilities would pass over to Public Shot &amp; its affiliates for any untoward complaints.";
        TextView textView = (TextView) view.findViewById(R.id.about_tv);
        if (mParam1.equals("aboutus")) {
            textView.setText(aboutus);
        }
        else{
            textView.setText(privaciPolicy);
        }
        textView.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

}
