package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/27/2017.
 */

public class BlogHome {


    private String imageName;
    private String blogPost;
    private String blogPostedBy;
    private String blogTime;

    public BlogHome() {

    }

    public BlogHome(String imageName, String blogPost, String blogPostedBy, String blogTime) {
        this.imageName = imageName;
        this.blogPost = blogPost;
        this.blogPostedBy = blogPostedBy;
        this.blogTime = blogTime;
    }

    public String getImageName() {
        return imageName;
    }

    public String getBlogPost() {
        return blogPost;
    }

    public String getBlogPostedBy() {
        return blogPostedBy;
    }

    public String getBlogTime() {
        return blogTime;
    }
}
