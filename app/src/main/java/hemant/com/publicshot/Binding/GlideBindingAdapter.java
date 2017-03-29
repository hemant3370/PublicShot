package hemant.com.publicshot.Binding;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import hemant.com.publicshot.R;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class GlideBindingAdapter {
    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).placeholder(ContextCompat.getDrawable(imageView.getContext(), R.drawable.com_facebook_profile_picture_blank_square)).fitCenter().into(imageView);
    }
}
