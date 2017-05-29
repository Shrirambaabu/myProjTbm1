package igotplaced.com.layouts.Model;

/**
 * Created by Ashith VL on 5/29/2017.
 */

public class Post {
    private String post;
    private String postIndustry;
    private String postImage;
    private String postProfileName;
    private String postTime;

    public Post() {
    }

    public Post(String post, String postIndustry, String postImage, String postProfileName, String postTime) {
        this.post = post;
        this.postIndustry = postIndustry;
        this.postImage = postImage;
        this.postProfileName = postProfileName;
        this.postTime = postTime;
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
