package igotplaced.com.layouts;

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

public class EventsPopUpActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private Intent intent;
    private String id = null;
    private  String  company,companyId,postedUserId;
    private Toolbar toolbar;
    private LinearLayoutManager mLayoutManager;
    private String userPostedComment;
    private RequestQueue queue;
    private NetworkImageView eventImage;
    private TextView eventName, eventTime,eventCompany, eventCaption, eventDesignation, eventVenue, eventDate, eventRegistered, eventStatus, eventMessage, eventIndustry;
    private String userId = null,userName=null,userImage = null,eventComentId;
    private EditText userComment;
    private ImageView sendComment;
    private String URL = BaseUri + "/home/eventsComments";
    private List<Events> eventList = new ArrayList<Events>();

    private RecyclerAdapterEventDetails recyclerAdapterEventDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        userImage = sharedpreferences.getString(UserImage, null);
        setContentView(R.layout.activity_events_pop_up);
        //initial value from intent
        initialization();
        setupToolbar();
        addressingView();
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getApplicationContext()).getRequestQueue();
        makeJsonEventRequest();
        addingListeners();
        postRecyclerView();

        makePostCommentsRequest();

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            Utils.showDialogue(EventsPopUpActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(EventsPopUpActivity.this);
        if (screenSize(EventsPopUpActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void addingListeners() {
        sendComment.setOnClickListener(this);
        eventName.setOnClickListener(this);
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
                        Events events = new Events(obj.getString("commentedUserImage"), obj.getString("comments"),obj.getString("id"),obj.getString("user_id"));
                        // adding movie to blogHomeList array
                        eventList.add(events);

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

    private void postRecyclerView() {

        RecyclerView eventRecycler = (RecyclerView) findViewById(R.id.comments_events_recycler);
        recyclerAdapterEventDetails = new RecyclerAdapterEventDetails(EventsPopUpActivity.this, eventList);
        //setting fixed size
        eventRecycler.setHasFixedSize(true);
        //setting horizontal layout
        eventRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) eventRecycler.getLayoutManager();
        //setting RecyclerView adapter
        eventRecycler.setAdapter(recyclerAdapterEventDetails);
    }

    private void makeJsonEventRequest() {

        Log.e("EventUrl",""+BaseUri + "/notificationService/questionPopUp/" + id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/notificationService/eventPopUp/" + id, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        Events events = new Events(obj.getString("id"), obj.getString("userid"), obj.getString("eventname"), obj.getString("eventtype"), obj.getString("location"), obj.getString("datetime"), obj.getString("reg_count"), obj.getString("event"), obj.getString("notes"), obj.getString("Industry"), obj.getString("imgname"), obj.getString("created_uname"), obj.getString("created_by"));


                        eventName.setText(events.getEventProfileName());
                        eventTime.setText(events.getEventTime());
                        eventMessage.setText(events.getEvent());
                        eventIndustry.setText("#"+events.getEventIndustry());
                        eventCompany.setText("");
                        eventCaption.setText(events.getEventCaption());
                        eventDesignation.setText(events.getEventDesignation());
                        eventVenue.setText(events.getEventVenue());
                        eventDate.setText(events.getEventDate());
                        eventRegistered.setText(events.getEventRegistered());
                        postedUserId=events.getEventUserId();
                        eventStatus.setText(events.getEventStatus());

                        Log.e("error", "Error: " + Utils.BaseImageUri + events.getEventImage());
                        eventImage.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(getApplicationContext()).getImageLoader());

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
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

    private void initialization() {
        intent = getIntent();

        id = intent.getStringExtra("eid");
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

                    Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_SHORT).show();
                }
                userComment.setText("");
                break;
            case R.id.event_profile_name:

                Intent otherProfileDetails=new Intent(getApplicationContext(), OtherProfileActivity.class);

                otherProfileDetails.putExtra("post_createdid", postedUserId);
                otherProfileDetails.putExtra("created_uname", eventName.getText().toString());
                startActivity(otherProfileDetails);
        }
    }

    private void insertUserComment() {


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                eventComentId=s;

                Events events = new Events(userImage, userPostedComment,eventComentId,userId);
                // adding movie to blogHomeList array
                eventList.add(events);
                recyclerAdapterEventDetails.notifyDataSetChanged();

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
                Utils.showDialogue(EventsPopUpActivity.this, "Sorry! Server Error");
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

        RequestQueue rQueue = Volley.newRequestQueue(EventsPopUpActivity.this);
        rQueue.add(request);

    }
}
