package publicshot.com.publicshot;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

import publicshot.com.publicshot.Model.FailedThumbnails;


/**
 * Created by hemantsingh on 28/03/17.
 */

public class ThumbnailExtract extends AsyncTask<String, Integer,Void> {

    public ThumbnailExtract() {

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            retriveVideoFrameFromVideo(params[0]);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    private static void retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        FailedThumbnails thumbnails = new FailedThumbnails(videoPath);
        Bitmap bitmap = null;
        File imageFile;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/publicshot_images");
            boolean success = true;
            if (!myDir.exists()) {
                success = myDir.mkdirs();
            }
            if (success) {
            String fname = FilenameUtils.getBaseName(new URL(videoPath).getPath()) + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) {
                    return ;
            } else {
                if (thumbnails.getFailedCount() < 2) {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    bitmap = mediaMetadataRetriever.getFrameAtTime(100000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    if (bitmap == null) {
                        thumbnails.incrementFailedCount();
                    }
                }
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            thumbnails.incrementFailedCount();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        if (bitmap != null) {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/publicshot_images");
            boolean success = true;
            if (!myDir.exists()) {
                success = myDir.mkdirs();
            }
            if (success) {
            String fname = FilenameUtils.getBaseName(new URL(videoPath).getPath()) + ".jpg";
             imageFile = new File(myDir, fname);
            try {
                FileOutputStream out = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
    }


}