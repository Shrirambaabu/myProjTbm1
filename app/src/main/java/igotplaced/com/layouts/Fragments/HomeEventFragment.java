package igotplaced.com.layouts.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfileEventDetails;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class HomeEventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private List<Events> eventList = new ArrayList<Events>();
    private RecyclerAdapterEventHome recyclerAdapterEventHome;

    int lastVisiblesItems, visibleItemCount, totalItemCount;
    int loadLimit;
    private LinearLayoutManager mLayoutManager;
    private boolean loading, swipe = false;

    public HomeEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        context = getActivity().getApplicationContext();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Events");
        //mapping web view
        mapping(view);

        //   mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        eventRecyclerView(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("LoaDScreen", "" + screenSize(getActivity()));
        if (screenSize(getActivity()) < 6.5) {
            loadLimit = 5;

        } else {

            loadLimit = 15;
        }

    }

    private void eventRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView event_view = (RecyclerView) view.findViewById(R.id.recycler_view_event);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterEventHome = new RecyclerAdapterEventHome(context, eventList);

        //setting fixed size
        Log.e("ScreenSizeReecyvlr", "" + screenSize(getActivity()));
        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        event_view.setHasFixedSize(true);
        //setting horizontal layout
        event_view.setLayoutManager(mLayoutManager);
        event_view.setAdapter(recyclerAdapterEventHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        //  loadData();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });

        event_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    lastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
/*

                    Log.d("error", ""+visibleItemCount+totalItemCount+lastVisiblesItems);
*/

                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }

                }
            }


        });

        //  recyclerAdapterEventHome.setClickListener(this);


    }

    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonObjectRequestEventHome(totalItemCount, totalItemCount + 5);

        recyclerAdapterEventHome.notifyDataSetChanged();

    }

    private void mapping(View view) {


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {

        eventList.clear();
        loadData();

    }

    private void loadData() {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

        makeJsonObjectRequestEventHome(0, loadLimit);

    }

    private void makeJsonObjectRequestEventHome(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        Log.d("error", "loaded" + BaseUri + "/home/topEvent/" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/home/topEvent/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {
/*
                Log.d("error", jsonObject.toString());*/
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");

                    //clearing blogList
                    // postList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Events event = new Events(obj.getString("id"), obj.getString("created_user"), obj.getString("eventname"), obj.getString("eventtype"), obj.getString("location"), obj.getString("datetime"), obj.getString("count"), obj.getString("event"), obj.getString("notes"), obj.getString("Industry"), obj.getString("eventImgName"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("imgname"), obj.getString("fname"), obj.getString("companyname"));
                            // adding movie to blogHomeList array
                            eventList.add(event);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterEventHome.notifyDataSetChanged();
                            loading = false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(getActivity(), "Sorry! Server Error");

            }
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view, int position) {

    }


    class RecyclerAdapterEventHome extends RecyclerView.Adapter<RecyclerAdapterEventHome.MyViewHolder> {


        private String userId = null, userName = null;
        private String URL = BaseUri + "/home/eventsComments";
        private String eventsId, postedEventsUserId, userPostedComment;

        private List<Events> eventsList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterEventHome(Context context, List<Events> eventsList) {

            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            userName = sharedpreferences.getString(Name, null);
            userId = sharedpreferences.getString(Id, null);

            this.context = context;
            this.eventsList = eventsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_event, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Events events = eventsList.get(position);

            eventsId = events.getEventId();
            postedEventsUserId = events.getEventUserId();

            holder.event_industry.setText("#" + events.getEventIndustry());
            //Pass the values of feeds object to Views
            holder.eventCaption.setText(events.getEventCaption());
            holder.eventDesignation.setText(events.getEventDesignation());
            holder.eventVenue.setText(events.getEventVenue());
            holder.eventDate.setText(events.getEventDate());
            holder.eventRegistered.setText(events.getEventRegistered());
            if (events.getEventCompany().equals("") || events.getEventCompany().equals("Select Company")) {
                holder.eventCompany.setText("");
            } else {
                holder.eventCompany.setText("#" + events.getEventCompany());
            }

            holder.eventStatus.setText(events.getEventStatus());
            holder.event.setText(events.getEvent());
            holder.event_profile_name.setText(events.getEventProfileName());

            holder.event_time.setText(events.getEventTime());

            //  holder.userImage.setImageUrl(Utils.BaseImageUri + events.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
            holder.event_img.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(context).getImageLoader());
            if (!userId.equals(postedEventsUserId)) {
                holder.event_profile_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);

                        otherProfileDetails.putExtra("post_createdid", eventsList.get(position).getEventUserId());
                        otherProfileDetails.putExtra("created_uname", eventsList.get(position).getEventProfileName());
                        startActivity(otherProfileDetails);
                    }
                });
            }
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileEventDetails = new Intent(getContext(), ProfileEventDetails.class);


                    profileEventDetails.putExtra("eid", eventsList.get(position).getEventId());
                    profileEventDetails.putExtra("ename", eventsList.get(position).getEventProfileName());
                    profileEventDetails.putExtra("eTime", eventsList.get(position).getEventTime());
                    profileEventDetails.putExtra("eCaption", eventsList.get(position).getEventCaption());
                    profileEventDetails.putExtra("eDesign", eventsList.get(position).getEventDesignation());
                    profileEventDetails.putExtra("eVenue", eventsList.get(position).getEventVenue());
                    profileEventDetails.putExtra("eDate", eventsList.get(position).getEventDate());
                    profileEventDetails.putExtra("eReg", eventsList.get(position).getEventRegistered());
                    profileEventDetails.putExtra("eStatus", eventsList.get(position).getEventStatus());
                    profileEventDetails.putExtra("eEvnt", eventsList.get(position).getEvent());
                    profileEventDetails.putExtra("eIndustry", eventsList.get(position).getEventIndustry());
                    profileEventDetails.putExtra("eImage", eventsList.get(position).getEventImage());
                    profileEventDetails.putExtra("eUserId", eventsList.get(position).getEventUserId());
                    profileEventDetails.putExtra("postCompany", eventsList.get(position).getEventCompany());

                    startActivity(profileEventDetails);
                }
            });


        }


        @Override
        public int getItemCount() {
            return eventsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView eventCaption, eventDesignation, eventVenue, eventCompany, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time, viewMore;
            private NetworkImageView event_img, userImage;


            public MyViewHolder(View itemView) {
                super(itemView);

                eventCaption = (TextView) itemView.findViewById(R.id.eventCaption);
                eventDesignation = (TextView) itemView.findViewById(R.id.eventDesignation);
                eventVenue = (TextView) itemView.findViewById(R.id.eventVenue);
                eventDate = (TextView) itemView.findViewById(R.id.eventDate);
                eventRegistered = (TextView) itemView.findViewById(R.id.eventRegistered);
                eventCompany = (TextView) itemView.findViewById(R.id.event_company);


                eventStatus = (TextView) itemView.findViewById(R.id.eventStatus);
                event = (TextView) itemView.findViewById(R.id.event);
                event_industry = (TextView) itemView.findViewById(R.id.event_industry);
                event_profile_name = (TextView) itemView.findViewById(R.id.event_profile_name);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

                event_time = (TextView) itemView.findViewById(R.id.event_time);
                // Volley's NetworkImageView which will load Image from URL
                event_img = (NetworkImageView) itemView.findViewById(R.id.event_img);
                // userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);


            }


        }
    }


}
