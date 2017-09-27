package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;


public class EventDetailsFragment extends Fragment implements View.OnClickListener {
    private String id = null, image = null, name = null, time = null, caption = null, designation = null, venue = null, date = null, registered = null, status = null, event = null, industry = null, postedUserId = null;

    private String userId = null, userName = null;
    private String userPostedComment;
    private String URL = BaseUri + "/home/eventsComments";

    private EditText userComment;

    private List<Events> eventList = new ArrayList<Events>();

    private RequestQueue queue;

    private RecyclerAdapterEventDetails recyclerAdapterEventDetails;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        addressingView(view);

        ImageView sendComment = (ImageView) view.findViewById(R.id.send_comment);
        sendComment.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("eid");
            image = bundle.getString("eImage");
            name = bundle.getString("ename");
            time = bundle.getString("eTime");
            caption = bundle.getString("eCaption");
            designation = bundle.getString("eDesign");
            venue = bundle.getString("eVenue");
            date = bundle.getString("eDate");
            registered = bundle.getString("eReg");
            status = bundle.getString("eStatus");
            event = bundle.getString("eEvnt");
            industry = bundle.getString("eIndustry");
            postedUserId = bundle.getString("eUserId");
        }

        NetworkImageView eventImage = (NetworkImageView) view.findViewById(R.id.event_img);
        TextView eventName = (TextView) view.findViewById(R.id.event_profile_name);
        TextView eventTime = (TextView) view.findViewById(R.id.event_time);
        TextView eventCaption = (TextView) view.findViewById(R.id.eventCaption);
        TextView eventDesignation = (TextView) view.findViewById(R.id.eventDesignation);
        TextView eventVenue = (TextView) view.findViewById(R.id.eventVenue);
        TextView eventDate = (TextView) view.findViewById(R.id.eventDate);
        TextView eventRegistered = (TextView) view.findViewById(R.id.eventRegistered);
        TextView eventStatus = (TextView) view.findViewById(R.id.eventStatus);
        TextView eventMessage = (TextView) view.findViewById(R.id.event);
        TextView eventIndustry = (TextView) view.findViewById(R.id.event_industry);

        eventImage.setImageUrl(Utils.BaseImageUri + image, NetworkController.getInstance(getContext()).getImageLoader());
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

        postRecyclerView(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void postRecyclerView(View view) {

        RecyclerView eventRecycler = (RecyclerView) view.findViewById(R.id.comments_events_recycler);
        recyclerAdapterEventDetails = new RecyclerAdapterEventDetails(getContext(), eventList);
        //setting fixed size
        eventRecycler.setHasFixedSize(true);
        //setting horizontal layout
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //setting RecyclerView adapter
        eventRecycler.setAdapter(recyclerAdapterEventDetails);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getContext()).getRequestQueue();

        makePostCommentsRequest();

    }

    private void makePostCommentsRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/home/eventCommentList/" + id, null, new Response.Listener<JSONArray>() {


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

                        Log.e("Comments", "" + obj.getString("comments"));

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
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void addressingView(View view) {

        userComment = (EditText) view.findViewById(R.id.user_comment);

    }


    @Override
    public void onClick(View v) {
        if (userComment.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the Comment", Toast.LENGTH_SHORT).show();
        } else {
            userPostedComment = userComment.getText().toString();
            insertUserComment();
            recyclerAdapterEventDetails.notifyDataSetChanged();
            Toast.makeText(getContext(), "Comment added", Toast.LENGTH_SHORT).show();
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
                Utils.showDialogue(getActivity(), "Sorry! Server Error");
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

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }


   /* class RecyclerAdapterEventDetails extends RecyclerView.Adapter<RecyclerAdapterEventDetails.MyViewHolder>{

        private List<Events> eventsList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterEventDetails(Context context, List<Events> eventsList) {

            this.context = context;
            this.eventsList = eventsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override
        public RecyclerAdapterEventDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.post_comments, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapterEventDetails.MyViewHolder holder, int position) {

            Events events = eventsList.get(position);
            holder.commentedPost.setText(events.getEventCommentMessage());
            holder.postImage.setImageUrl(Utils.BaseImageUri + events.getEventCommentImage(), NetworkController.getInstance(context).getImageLoader());

        }

        @Override
        public int getItemCount() {
            return eventsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private NetworkImageView postImage;
            private TextView commentedPost;

            public MyViewHolder(View itemView) {
                super(itemView);
                postImage=(NetworkImageView) itemView.findViewById(R.id.post_img);
                commentedPost=(TextView) itemView.findViewById(R.id.commented_post);
            }
        }
    }*/
}
