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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterInterviewDetails;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class ProfileInterviewDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String id = null, name = null, time = null, interview = null, image = null, industry = null, interviewUserId = null;

    private NetworkImageView interviewImage;
    private TextView profileName, profileTime, interviewMessage, interviewIndustry;

    private EditText userComment;
    private ImageView sendComment;
    private LinearLayoutManager mLayoutManager;

    private RecyclerAdapterInterviewDetails recyclerAdapterInterviewDetails;
    private RequestQueue queue;
    private Intent intent;
    private Toolbar toolbar;
    private List<Interview> interviewList = new ArrayList<Interview>();

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/interviewComments";
    private String userPostedComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);


        setContentView(R.layout.activity_profile_interview_details);
        setupToolbar();
        addressingView();
        addingListeners();
        //initial value from intent
        initialization();

        interviewImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getApplicationContext()).getImageLoader());
        profileName.setText(name);
        profileTime.setText(time);
        interviewMessage.setText(interview);
        interviewIndustry.setText(industry);

        postRecyclerView();

        makePostCommentsRequest();

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
            actionBar.setTitle("Interview Experience");
        }
    }

    private void makePostCommentsRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/interviewCommentList/" + id, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                interviewList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Interview interview = new Interview(obj.getString("commentedUserImage"), obj.getString("comments"));
                        // adding movie to blogHomeList array
                        interviewList.add(interview);

                        Log.e("Comments", "" + obj.getString("comments"));

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterInterviewDetails.notifyDataSetChanged();
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

    private void postRecyclerView() {

        RecyclerView postRecycler = (RecyclerView) findViewById(R.id.comments_interview_recycler);
        recyclerAdapterInterviewDetails = new RecyclerAdapterInterviewDetails(getApplicationContext(), interviewList);
        //setting fixed size
        postRecycler.setHasFixedSize(true);
        //setting horizontal layout
        postRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) postRecycler.getLayoutManager();
        //setting RecyclerView adapter
        postRecycler.setAdapter(recyclerAdapterInterviewDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getApplicationContext()).getRequestQueue();
    }

    private void addressingView() {


        interviewImage = (NetworkImageView) findViewById(R.id.interview_img);
        profileName = (TextView) findViewById(R.id.interview_profile_name);
        profileTime = (TextView) findViewById(R.id.interview_time);
        interviewMessage = (TextView) findViewById(R.id.interview);
        interviewIndustry = (TextView) findViewById(R.id.interview_industry);
        userComment = (EditText) findViewById(R.id.user_comment);
        sendComment = (ImageView) findViewById(R.id.send_comment);
    }

    private void addingListeners() {
        sendComment.setOnClickListener(this);
    }

    private void initialization() {
        intent = getIntent();

        id = intent.getStringExtra("iid");
        name = intent.getStringExtra("created_uname");
        time = intent.getStringExtra("created_by");
        interview = intent.getStringExtra("interview");
        image = intent.getStringExtra("interviewImage");
        industry = intent.getStringExtra("interviewIndustry");
        interviewUserId = intent.getStringExtra("interview_createdid");
    }

    @Override
    public void onClick(View v) {
        if (userComment.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter the Comment", Toast.LENGTH_SHORT).show();
        } else {
            userPostedComment = userComment.getText().toString();
            insertUserComment();
            recyclerAdapterInterviewDetails.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_SHORT).show();
        }
        userComment.setText("");
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
                Utils.showDialogue(ProfileInterviewDetailsActivity.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("iid", id);
                parameters.put("intex_createrid", interviewUserId);
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

        RequestQueue rQueue = Volley.newRequestQueue(ProfileInterviewDetailsActivity.this);
        rQueue.add(request);
    }
}
