package hemant.com.publicshot.Binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class GlideBindingAdapter {
    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).fitCenter().into(imageView);
    }
}
