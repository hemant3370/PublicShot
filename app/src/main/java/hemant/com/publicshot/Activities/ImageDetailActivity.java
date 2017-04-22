package hemant.com.publicshot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import hemant.com.publicshot.Constants;
import hemant.com.publicshot.Model.FeedItem;
import hemant.com.publicshot.databinding.ActivityImageDetailBinding;

public class ImageDetailActivity extends AppCompatActivity {
    FeedItem thisItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityImageDetailBinding binding = ActivityImageDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         thisItem = getIntent().getParcelableExtra("item");
        binding.setData(thisItem);
        if (binding.getData().getType().equals("image")){
            Glide.with(this).load(Constants.MyUrl.BASE_URL + thisItem.getMediaUrl())
                    .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView);
        }
        else {
            binding.imageView.setVisibility(View.GONE);
            binding.videoplayer.setVisibility(View.VISIBLE);
            binding.videoplayer.setUp(Constants.MyUrl.BASE_URL + thisItem.getMediaUrl()
                    , JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, thisItem.getName());
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JCVideoPlayerStandard.releaseAllVideos();
                    JCVideoPlayerStandard.backPress();
                    ImageDetailActivity.super.onBackPressed();
                }
            };
            binding.videoplayer.backButton.setOnClickListener(listener);
            binding.videoplayer.tinyBackImageView.setOnClickListener(listener);
            binding.videoplayer.titleTextView.setOnClickListener(listener);
        }
    }
    public void closeImage(View view){
        super.onBackPressed();
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
