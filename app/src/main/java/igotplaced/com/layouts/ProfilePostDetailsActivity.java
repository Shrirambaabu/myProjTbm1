package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.UserImage;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class ProfilePostDetailsActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {


    private String id = null, name = null, time = null, companyId, post = null, image = null, industry = null, postUserId = null, Company = null;

    private NetworkImageView postImage;
    private TextView profileName, profileTime, postMessage, postIndustry, postCompany;

    private EditText userComment;
    private ImageView sendComment;

    private List<Post> postList = new ArrayList<Post>();

    private RequestQueue queue;
    private RecyclerAdapterPostDetails recyclerAdapterPostDetails;

    private String userId = null, userName = null, userImage = null,postId;
    private String URL = BaseUri + "/home/postComments";
    private String userPostedComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        userImage = sharedpreferences.getString(UserImage, null);

        setContentView(R.layout.activity_profile_post_details);
        //initial value from intent
        initialization();
        setupToolbar();
        addressingView();
        addingListeners();

        ProgressDialog pDialog = new ProgressDialog(ProfilePostDetailsActivity.this, R.style.MyThemeProgress);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pDialog.onBackPressed();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        postImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getApplicationContext()).getImageLoader());
        profileName.setText(name);
        profileTime.setText(time);
        postMessage.setText(post);
        postIndustry.setText("#" + industry);
        if (Company.equals("")) {
            postCompany.setText(Company);
        } else {
            postCompany.setText("#" + Company);
        }


        postRecyclerView();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Utils.showDialogue(ProfilePostDetailsActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(ProfilePostDetailsActivity.this);
        if (screenSize(ProfilePostDetailsActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setTitle(" Posts");
        }
    }

    private void postRecyclerView() {
        RecyclerView postRecycler = (RecyclerView) findViewById(R.id.comments_post_recycler);
        recyclerAdapterPostDetails = new RecyclerAdapterPostDetails(ProfilePostDetailsActivity.this, postList);
        //setting fixed size
        postRecycler.setHasFixedSize(true);
        //setting horizontal layout
        postRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //setting RecyclerView adapter
        postRecycler.setAdapter(recyclerAdapterPostDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getApplicationContext()).getRequestQueue();

        makePostCommentsRequest();

    }

    private void makePostCommentsRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/postCommentList/" + id, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                postList.clear();

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Post post = new Post(obj.getString("commentedUserImage"), obj.getString("comments"),obj.getString("id"),obj.getString("user_id"));
                        // adding movie to blogHomeList array
                        postList.add(post);

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
                Utils.showDialogue(ProfilePostDetailsActivity.this, "Sorry! Server Error");
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
        userComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.user_comment) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                return false;
            }
        });
        sendComment = (ImageView) findViewById(R.id.send_comment);
    }

    private void initialization() {
        Intent intent = getIntent();
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
        switch (v.getId()) {
            case R.id.send_comment:
                if (userComment.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the Comment", Toast.LENGTH_SHORT).show();
                } else {
                    userPostedComment = userComment.getText().toString();
                    insertUserComment();

                }
                userComment.setText("");
                break;
            case R.id.post_profile_name:

                Intent otherProfileDetails = new Intent(getApplicationContext(), OtherProfileActivity.class);

                otherProfileDetails.putExtra("post_createdid", postUserId);
                otherProfileDetails.putExtra("created_uname", name);
                startActivity(otherProfileDetails);
                break;

            case R.id.post_company:

                Intent companyDetails = new Intent(getApplicationContext(), CompanyDetailsActivity.class);
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

                Log.e("InserString",""+s);
                postId=s;
                Post post = new Post(userImage, userPostedComment,postId,userId);
                // adding movie to blogHomeList array
                postList.add(post);
                recyclerAdapterPostDetails.notifyDataSetChanged();
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
