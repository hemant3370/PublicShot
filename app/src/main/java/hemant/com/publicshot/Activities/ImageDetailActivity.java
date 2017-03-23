package hemant.com.publicshot.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import hemant.com.publicshot.R;

public class ImageDetailActivity extends AppCompatActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.title_image)
    TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        titleTextView.setText(getIntent().getStringExtra("title"));
        Glide.with(this).load(getIntent().getStringExtra("url")).crossFade()
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView);
    }
}
