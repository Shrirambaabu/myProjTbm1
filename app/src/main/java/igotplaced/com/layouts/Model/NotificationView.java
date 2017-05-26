package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/26/2017.
 */

public class NotificationView {

    private String createdBy;
    private String postId;
    private String caption;
    private String notificationPost;
    private String fName;
    private String ImageName;

    public NotificationView() {
    }

    public NotificationView(String createdBy, String postId, String caption, String notificationPost, String fName, String imageName) {
        this.createdBy = createdBy;
        this.postId = postId;
        this.caption = caption;
        this.notificationPost = notificationPost;
        this.fName = fName;
        this.ImageName = imageName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getPostId() {
        return postId;
    }

    public String getCaption() {
        return caption;
    }

    public String getNotificationPost() {
        return notificationPost;
    }

    public String getfName() {
        return fName;
    }

    public String getImageName() {
        return ImageName;
    }
}