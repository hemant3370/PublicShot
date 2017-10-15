package publicshot.com.publicshot.Activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import publicshot.com.publicshot.Constants;
import publicshot.com.publicshot.Model.FeedItem;
import publicshot.com.publicshot.databinding.ActivityImageDetailBinding;

public class ImageDetailActivity extends AppCompatActivity {
    FeedItem thisItem;
    ProgressDialog progressBar;
    private long enqueue;
    private DownloadManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityImageDetailBinding binding = ActivityImageDetailBinding.inflate(getLayoutInflater());
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
            File direct = new File(Environment.getExternalStorageDirectory()
                    + "/publicshot/" + thisItem.getId());

            if (direct.exists()) {
                binding.videoplayer.setVideoPath(direct.getPath()).getPlayer().start();
            }
            else {
                progressBar=new ProgressDialog(this);
                progressBar.setMessage("Loading...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setIndeterminate(true);
                progressBar.show();
                BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                            long downloadId = intent.getLongExtra(
                                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(enqueue);
                            Cursor c = dm.query(query);
                            if (c.moveToFirst()) {
                                int columnIndex = c
                                        .getColumnIndex(DownloadManager.COLUMN_STATUS);
                                if (DownloadManager.STATUS_SUCCESSFUL == c
                                        .getInt(columnIndex)) {

                                    String uriString = c
                                            .getString(c
                                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                    progressBar.dismiss();
                                    binding.videoplayer.setVideoPath(uriString).getPlayer().start();
                                }
                            }
                        }
                    }
                };

                registerReceiver(receiver, new IntentFilter(
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(Constants.MyUrl.BASE_URL + thisItem.getMediaUrl()));
                request.setDestinationInExternalPublicDir("/publicshot", thisItem.getId());
                enqueue = dm.enqueue(request);
            }
        }
    }
    public void closeImage(View view){
        super.onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
