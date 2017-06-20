package publicshot.com.publicshot.Model;


/**
 * Created by hemantsingh on 22/04/17.
 */

public class FailedThumbnails  {
    private String url;
    private Integer failedCount;

    public Integer getFailedCount()
    {
     if (this.failedCount == null) return 0;
        return failedCount;
    }
     public void incrementFailedCount(){
         if (this.failedCount == null) failedCount = 0;
         this.failedCount += 1;
     }
    public FailedThumbnails(String url) {
        this.url = url;
    }

    public FailedThumbnails() {
    }
}
