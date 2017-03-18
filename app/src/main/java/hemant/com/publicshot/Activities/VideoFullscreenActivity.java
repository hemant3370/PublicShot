package hemant.com.publicshot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
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
//        jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
