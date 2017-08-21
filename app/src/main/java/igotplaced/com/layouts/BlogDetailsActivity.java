package igotplaced.com.layouts;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class BlogDetailsActivity extends AppCompatActivity {

    private RequestQueue queue;
    private Intent intent;
    private String postId;
    private Toolbar toolbar;

    private WebView webPost;
    private TextView blogPost, blogPostedBy;
    private NetworkImageView blog_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        setupToolbar();

        //initial value from intent
        initialization();
        //mapping web view
        mapping();

        queue = NetworkController.getInstance(BlogDetailsActivity.this).getRequestQueue();

        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestBlogDetails(postId);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Blog");
        }
    }

    private void mapping() {

        webPost = (WebView) findViewById(R.id.blog_post);
        blogPost = (TextView) findViewById(R.id.blog_post_head);
        blogPostedBy = (TextView) findViewById(R.id.blog_posted_by);
        // Volley's NetworkImageView which will load Image from URL
        blog_img = (NetworkImageView) findViewById(R.id.blog_img);
    }

    private void makeJsonArrayRequestBlogDetails(String postId) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/blogService/blogView/"+postId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        BlogHome blogView = new BlogHome(obj.getString("image"), obj.getString("header"), obj.getString("author"), obj.getString("contents"));

                        blogPost.setText(obj.getString("header"));
                        blogPostedBy.setText("Posted by: "+obj.getString("author"));
                        blog_img.setImageUrl(Utils.BaseImageUri + blogView.getImageName(), NetworkController.getInstance(BlogDetailsActivity.this).getImageLoader());
                        webPost.loadDataWithBaseURL(null, obj.getString("contents"), "text/html", "utf-8", null);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());

            }
        });

        queue.add(jsonArrayRequest);

    }

    private void initialization() {
        intent = getIntent();
        //getting value from intent
        postId = intent.getStringExtra("postId");
    }
}
