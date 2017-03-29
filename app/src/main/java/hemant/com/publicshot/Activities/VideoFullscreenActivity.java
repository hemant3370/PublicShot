package hemant.com.publicshot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import hemant.com.publicshot.Constants;
import hemant.com.publicshot.Model.FeedItem;
import hemant.com.publicshot.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoFullscreenActivity extends AppCompatActivity {

   FeedItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_fullscreen);
        item = getIntent().getParcelableExtra("video");
        // Set up the user interaction to manually show or hide the s
        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);

        jcVideoPlayerStandard.setUp(Constants.MyUrl.BASE_URL + item.getMediaUrl()
                , JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, item.getName());
        jcVideoPlayerStandard.startVideo();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JCVideoPlayerStandard.releaseAllVideos();
                JCVideoPlayerStandard.backPress();
                VideoFullscreenActivity.super.onBackPressed();
            }
        };
        jcVideoPlayerStandard.backButton.setOnClickListener(listener);
        jcVideoPlayerStandard.tinyBackImageView.setOnClickListener(listener);
        jcVideoPlayerStandard.titleTextView.setOnClickListener(listener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    @Override
    public void onBackPressed() {
     JCVideoPlayerStandard.backPress();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }


}
