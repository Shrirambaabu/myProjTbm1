package igotplaced.com.layouts.Model;

/**
 * Created by Admin on 5/27/2017.
 */

public class BlogHome {


    private String imageName;
    private String blogPost;
    private String blogPostedBy;
    private String blogTime;

    private String contents;

    private String id;


    public BlogHome() {

    }

    public BlogHome(String imageName, String blogPost, String blogPostedBy, String blogTime, String id) {
        this.imageName = imageName;
        this.blogPost = blogPost;
        this.blogPostedBy = blogPostedBy;
        this.blogTime = blogTime;
        this.id = id;
    }

    public BlogHome(String imageName, String blogPost, String blogPostedBy, String contents) {
        this.imageName = imageName;
        this.blogPost = blogPost;
        this.blogPostedBy = blogPostedBy;
        this.contents = contents;
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

    public String getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

}
