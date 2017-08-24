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

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterEventDetails;
import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class ProfileEventDetails extends AppCompatActivity implements View.OnClickListener {

    private String id = null, image = null, name = null, time = null, caption = null, company=null,designation = null, venue = null, date = null, registered = null, status = null, event = null, industry = null, postedUserId = null;

    private String userId = null, userName = null;
    private String userPostedComment;
    private String URL = BaseUri + "/home/eventsComments";

    private NetworkImageView eventImage;
    private TextView eventName, eventTime,eventCompany, eventCaption, eventDesignation, eventVenue, eventDate, eventRegistered, eventStatus, eventMessage, eventIndustry;

    private EditText userComment;
    private ImageView sendComment;

    private List<Events> eventList = new ArrayList<Events>();
    private LinearLayoutManager mLayoutManager;

    private RequestQueue queue;
    private Intent intent;
    private Toolbar toolbar;

    private RecyclerAdapterEventDetails recyclerAdapterEventDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        setContentView(R.layout.activity_profile_event_details);
        //initial value from intent
        initialization();
        setupToolbar();
        addressingView();
        addingListeners();


        eventImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getApplicationContext()).getImageLoader());
        eventName.setText(name);
        eventTime.setText(time);
        eventCaption.setText(caption);
        eventDesignation.setText(designation);
        eventVenue.setText(venue);
        eventDate.setText(date);
        eventRegistered.setText(registered);
        eventStatus.setText(status);
        eventMessage.setText(event);
        eventIndustry.setText(industry);
        eventCompany.setText("");

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
            actionBar.setTitle("Events");
        }
    }

    private void postRecyclerView() {

        RecyclerView eventRecycler = (RecyclerView) findViewById(R.id.comments_events_recycler);
        recyclerAdapterEventDetails = new RecyclerAdapterEventDetails(getApplicationContext(), eventList);
        //setting fixed size
        eventRecycler.setHasFixedSize(true);
        //setting horizontal layout
        eventRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) eventRecycler.getLayoutManager();
        //setting RecyclerView adapter
        eventRecycler.setAdapter(recyclerAdapterEventDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getApplicationContext()).getRequestQueue();

        makePostCommentsRequest();
    }

    private void makePostCommentsRequest() {

        Log.e("URL",""+ BaseUri + "/home/eventCommentList/" + id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/eventCommentList/" + id, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                eventList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {


                        JSONObject obj = response.getJSONObject(i);
                        Events events = new Events(obj.getString("commentedUserImage"), obj.getString("comments"));
                        // adding movie to blogHomeList array
                        eventList.add(events);

                        Log.e("Comments",""+ obj.getString("comments"));

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterEventDetails.notifyDataSetChanged();
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

    private void addressingView() {

        eventImage = (NetworkImageView) findViewById(R.id.event_img);
        eventName = (TextView) findViewById(R.id.event_profile_name);
        eventTime = (TextView) findViewById(R.id.event_time);
        eventCaption = (TextView) findViewById(R.id.eventCaption);
        eventDesignation = (TextView) findViewById(R.id.eventDesignation);
        eventVenue = (TextView) findViewById(R.id.eventVenue);
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventRegistered = (TextView) findViewById(R.id.eventRegistered);
        eventStatus = (TextView) findViewById(R.id.eventStatus);
        eventMessage = (TextView) findViewById(R.id.event);
        eventIndustry = (TextView) findViewById(R.id.event_industry);
        eventCompany = (TextView) findViewById(R.id.event_company);
        userComment = (EditText) findViewById(R.id.user_comment);
        sendComment = (ImageView) findViewById(R.id.send_comment);
    }

    private void addingListeners() {
        sendComment.setOnClickListener(this);
        eventName.setOnClickListener(this);
    }

    private void initialization() {
        intent = getIntent();

        id = intent.getStringExtra("eid");
        image = intent.getStringExtra("eImage");
        name =intent.getStringExtra("ename");
        time = intent.getStringExtra("eTime");
        caption = intent.getStringExtra("eCaption");
        designation = intent.getStringExtra("eDesign");
        venue = intent.getStringExtra("eVenue");
        date = intent.getStringExtra("eDate");
        registered = intent.getStringExtra("eReg");
        status = intent.getStringExtra("eStatus");
        event = intent.getStringExtra("eEvnt");
        industry = intent.getStringExtra("eIndustry");
        postedUserId = intent.getStringExtra("eUserId");

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
                    recyclerAdapterEventDetails.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_SHORT).show();
                }
                userComment.setText("");
                break;
            case R.id.event_profile_name:

                Intent otherProfileDetails=new Intent(getApplicationContext(), OtherProfileActivity.class);

                otherProfileDetails.putExtra("post_createdid", postedUserId);
                otherProfileDetails.putExtra("created_uname", name);
                startActivity(otherProfileDetails);
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
                Utils.showDialogue(ProfileEventDetails.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("eid", id);
                parameters.put("evt_createrid", postedUserId);
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

        RequestQueue rQueue = Volley.newRequestQueue(ProfileEventDetails.this);
        rQueue.add(request);
    }
}
