package igotplaced.com.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterPostDetails;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class ProfilePostDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private String id = null, name = null, time = null,companyId, post = null, image = null, industry = null, postUserId = null,Company=null;

    private NetworkImageView postImage;
    private TextView profileName, profileTime, postMessage, postIndustry,postCompany;

    private EditText userComment;
    private ImageView sendComment;

    private List<Post> postList = new ArrayList<Post>();
    private LinearLayoutManager mLayoutManager;

    private RequestQueue queue;
    private Intent intent;

    private RecyclerAdapterPostDetails recyclerAdapterPostDetails;

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/postComments";
    private String userPostedComment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        setContentView(R.layout.activity_profile_post_details);
        //initial value from intent
        initialization();
        setupToolbar();
        addressingView();
        addingListeners();



        postImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getApplicationContext()).getImageLoader());
        profileName.setText(name);
        profileTime.setText(time);
        postMessage.setText(post);
        postIndustry.setText("#"+industry);
        if (Company.equals("")){
            postCompany.setText(Company);
        }else{
            postCompany.setText("#"+Company);
        }


        postRecyclerView();

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
            actionBar.setTitle("Posts");
        }
    }

    private void postRecyclerView() {
        RecyclerView postRecycler = (RecyclerView) findViewById(R.id.comments_post_recycler);
        recyclerAdapterPostDetails = new RecyclerAdapterPostDetails(getApplicationContext(), postList);
        //setting fixed size
        postRecycler.setHasFixedSize(true);
        //setting horizontal layout
        postRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) postRecycler.getLayoutManager();
        //setting RecyclerView adapter
        postRecycler.setAdapter(recyclerAdapterPostDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getApplicationContext()).getRequestQueue();

        makePostCommentsRequest();
    }

    private void makePostCommentsRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/postCommentList/" + id, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                postList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Post post = new Post(obj.getString("commentedUserImage"), obj.getString("comments"));
                        // adding movie to blogHomeList array
                        postList.add(post);

                        Log.e("Comments",""+ obj.getString("comments"));

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterPostDetails.notifyDataSetChanged();
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

    private void addingListeners() {
        sendComment.setOnClickListener(this);
        profileName.setOnClickListener(this);
        postCompany.setOnClickListener(this);
    }

    private void addressingView() {

        postImage = (NetworkImageView) findViewById(R.id.post_img);
        profileName = (TextView) findViewById(R.id.post_profile_name);
        profileTime = (TextView) findViewById(R.id.post_time);
        postMessage = (TextView) findViewById(R.id.post);
        postIndustry = (TextView) findViewById(R.id.post_industry);
        postCompany = (TextView) findViewById(R.id.post_company);
        userComment = (EditText) findViewById(R.id.user_comment);
        sendComment = (ImageView) findViewById(R.id.send_comment);
    }

    private void initialization() {
        intent = getIntent();
        //getting value from intent
        id = intent.getStringExtra("pid");
        name = intent.getStringExtra("created_uname");
        time = intent.getStringExtra("created_by");
        post = intent.getStringExtra("post");
        image = intent.getStringExtra("postImage");
        industry = intent.getStringExtra("postIndustry");
        postUserId = intent.getStringExtra("post_createdid");
        Company = intent.getStringExtra("postCompany");
        companyId = intent.getStringExtra("companyId");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_comment:
                if (userComment.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the Comment", Toast.LENGTH_SHORT).show();
                } else {
                    userPostedComment = userComment.getText().toString();
                    insertUserComment();
                    recyclerAdapterPostDetails.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_SHORT).show();
                }
                userComment.setText("");
                break;
            case R.id.post_profile_name:

                Intent otherProfileDetails=new Intent(getApplicationContext(), OtherProfileActivity.class);

                otherProfileDetails.putExtra("post_createdid", postUserId);
                otherProfileDetails.putExtra("created_uname", name);
                startActivity(otherProfileDetails);
                break;

            case R.id.post_company:

                Intent companyDetails=new Intent(getApplicationContext(),CompanyDetailsActivity.class);
                companyDetails.putExtra("postCompany", Company);
                companyDetails.putExtra("companyId", companyId);
                startActivity(companyDetails);
                break;


        }


    }

    private void insertUserComment() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(ProfilePostDetailsActivity.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("pid", id);
                parameters.put("post_createdid", postUserId);
                parameters.put("user_id", userId);
                parameters.put("comments", userPostedComment);
                parameters.put("created_uname", userName);
                Log.e("param", "" + parameters);
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(ProfilePostDetailsActivity.this);
        rQueue.add(request);
    }
}
