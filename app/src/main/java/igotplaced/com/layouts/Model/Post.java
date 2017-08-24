package igotplaced.com.layouts.Model;

/**
 * Created by Ashith VL on 5/29/2017.
 */

public class Post {
    private String postId;
    private String postedUserId;
    private String post;
    private String postIndustry;
    private String postImage;
    private String postProfileName;
    private String postTime;
    private String userImage;
    private String userProfileName;
    private String comentedUserImage;
    private String commentedMessage;
    private String postCompany;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public Post(String comenteduserImage, String commentedMessage) {
        this.comentedUserImage = comenteduserImage;
        this.commentedMessage = commentedMessage;
    }

    public Post() {
    }

    public Post(String postId, String postedUserId, String post, String postIndustry, String postImage, String postProfileName, String postTime, String userImage, String userProfileName,String postCompany,String companyId) {
        this.postId = postId;
        this.postedUserId = postedUserId;
        this.post = post;
        this.postIndustry = postIndustry;
        this.postImage = postImage;
        this.postProfileName = postProfileName;
        this.postTime = postTime;
        this.userImage = userImage;
        this.userProfileName = userProfileName;
        this.postCompany = postCompany;
        this.companyId = companyId;
    }

    public Post(String postId, String post, String postIndustry, String postImage, String postProfileName, String postTime, String userImage, String userProfileName) {
        this.postId = postId;
        this.post = post;
        this.postIndustry = postIndustry;
        this.postImage = postImage;
        this.postProfileName = postProfileName;
        this.postTime = postTime;
        this.userImage = userImage;
        this.userProfileName = userProfileName;
    }

    public Post(String post, String postIndustry, String postImage, String postProfileName, String postTime) {
        this.post = post;
        this.postIndustry = postIndustry;
        this.postImage = postImage;
        this.postProfileName = postProfileName;
        this.postTime = postTime;
    }

    public Post(String post, String postIndustry, String postImage, String postProfileName, String postTime, String userImage, String userProfileName) {
        this.post = post;
        this.postIndustry = postIndustry;
        this.postImage = postImage;
        this.postProfileName = postProfileName;
        this.postTime = postTime;
        this.userImage = userImage;
        this.userProfileName = userProfileName;
    }

    public String getPostCompany() {
        return postCompany;
    }

    public String getComentedUserImage() {
        return comentedUserImage;
    }

    public String getCommentedMessage() {
        return commentedMessage;
    }

    public String getPostedUserId() {
        return postedUserId;
    }

    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserProfileName() {
        return userProfileName;
    }

    public String getPost() {
        return post;
    }

    public String getPostIndustry() {
        return postIndustry;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostProfileName() {
        return postProfileName;
    }

    public String getPostTime() {
        return postTime;
    }
}
