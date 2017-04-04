package hemant.com.publicshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by hemantsingh on 28/03/17.
 */

public class ThumbnailExtract extends AsyncTask<String, Integer, Bitmap> {

    private final ImageView mThumbnail;
    private Bitmap mDefaultBitmap;


    public ThumbnailExtract(String videoLocalUrl, ImageView thumbnail, Bitmap defaultBitmap) {
        String videoUrl = videoLocalUrl;
        mThumbnail = thumbnail;
        mDefaultBitmap = defaultBitmap;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            return retriveVideoFrameFromVideo(params[0]);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return mDefaultBitmap;
        }
    }

    @Override
    protected void onPostExecute(Bitmap thumb) {
        if (thumb != null) {
            mThumbnail.setImageBitmap(thumb);
        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/publicshot_images");
            myDir.mkdirs();

            String fname = FilenameUtils.getBaseName(new URL(videoPath).getPath()) + ".jpg";
            File file = new File (myDir, fname);
            if (file.exists ()) {
                Bitmap b = BitmapFactory. decodeFile(file.getAbsolutePath());
                if (b != null){
                    return b;
                }
                else {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    bitmap = mediaMetadataRetriever.getFrameAtTime();
                }
            }
            else {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                bitmap = mediaMetadataRetriever.getFrameAtTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        if (bitmap != null){
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/publicshot_images");
            myDir.mkdirs();

            String fname = FilenameUtils.getBaseName(new URL(videoPath).getPath()) + ".jpg";
            File file = new File (myDir, fname);
            if (file.exists ()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


}