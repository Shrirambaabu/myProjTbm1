package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfileEventDetails;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.screenSize;


public class CompanyEventFragment extends Fragment {
    private TextView noData;
    private Context context;
    private RequestQueue queue;
    private List<Events> eventsList = new ArrayList<Events>();
    private String userId;
    private LinearLayoutManager mLayoutManager;
    private RecyclerCompanyEvents recyclerCompanyEvents;

    public CompanyEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_event, container, false);

        context = getActivity().getApplicationContext();
        //  mLayoutManager = new LinearLayoutManager(context);
        noData=(TextView) view.findViewById(R.id.no_data);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            userId = bundle.getString("otherId");
        }
        eventRecyclerView(view);
        return view;
    }

    private void eventRecyclerView(View view) {

        RecyclerView event_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_events);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerCompanyEvents = new RecyclerCompanyEvents(context, eventsList);

        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        event_view.setHasFixedSize(true);
        //setting horizontal layout
        event_view.setLayoutManager(mLayoutManager);
        event_view.setAdapter(recyclerCompanyEvents);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        makeJsonArrayRequestEventHome();
    }

    private void makeJsonArrayRequestEventHome() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/companyProfileEvent/" + userId, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                eventsList.clear();
                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        Events events = new Events(obj.getString("eid"), obj.getString("created_user"), obj.getString("eventname"), obj.getString("eventtype"), obj.getString("location"), obj.getString("datetime"), obj.getString("count"), obj.getString("event"), obj.getString("notes"), obj.getString("Industry"), obj.getString("companyEventImage"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("companyEventImage"), obj.getString("created_uname"), obj.getString("companyname"));
                        // adding movie to blogHomeList array
                        eventsList.add(events);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerCompanyEvents.notifyDataSetChanged();
                    }
                }
                if (eventsList.isEmpty()){
                    noData.setVisibility(View.VISIBLE);
                }else {
                    noData.setVisibility(View.GONE);
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(getActivity(),"Sorry! Server Error");
            }
        });

        queue.add(jsonArrayRequest);
    }

    private class RecyclerCompanyEvents extends RecyclerView.Adapter<RecyclerCompanyEvents.MyViewHolder> {

        private List<Events> eventsList;
        private Context context;
        private LayoutInflater inflater;
        private String id;
        private SharedPreferences sharedpreferences;

        public RecyclerCompanyEvents(Context context, List<Events> eventsList) {

            this.context = context;
            this.eventsList = eventsList;
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            id = sharedpreferences.getString(Id, null);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public RecyclerCompanyEvents.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_event, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerCompanyEvents.MyViewHolder holder, final int position) {

            Events events = eventsList.get(position);
            //Pass the values of feeds object to Views
            holder.eventCaption.setText(events.getEventCaption());
            holder.eventDesignation.setText(events.getEventDesignation());
            holder.eventVenue.setText(events.getEventVenue());
            holder.eventDate.setText(events.getEventDate());
            holder.eventRegistered.setText(events.getEventRegistered());
            holder.eventCompany.setText(events.getEventCompany());
            holder.eventStatus.setText(events.getEventStatus());
            holder.event.setText(events.getEvent());
            holder.event_industry.setText("#" + events.getEventIndustry());
            holder.event_profile_name.setText(events.getEventProfileName());

            holder.event_time.setText(events.getEventTime());

            //  holder.userImage.setImageUrl(Utils.BaseImageUri + events.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
            holder.event_img.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(context).getImageLoader());

            if (!id.equals(eventsList.get(position).getEventUserId())) {
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

            private TextView eventCaption, eventDesignation, eventVenue, viewMore, eventCompany, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time;
            private NetworkImageView event_img;


            public MyViewHolder(View itemView) {
                super(itemView);

                eventCaption = (TextView) itemView.findViewById(R.id.eventCaption);
                eventDesignation = (TextView) itemView.findViewById(R.id.eventDesignation);
                eventVenue = (TextView) itemView.findViewById(R.id.eventVenue);
                eventDate = (TextView) itemView.findViewById(R.id.eventDate);
                eventRegistered = (TextView) itemView.findViewById(R.id.eventRegistered);
                eventCompany = (TextView) itemView.findViewById(R.id.event_company);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);
                eventStatus = (TextView) itemView.findViewById(R.id.eventStatus);
                event = (TextView) itemView.findViewById(R.id.event);
                event_industry = (TextView) itemView.findViewById(R.id.event_industry);
                event_profile_name = (TextView) itemView.findViewById(R.id.event_profile_name);
                event_time = (TextView) itemView.findViewById(R.id.event_time);
                // Volley's NetworkImageView which will load Image from URL
                event_img = (NetworkImageView) itemView.findViewById(R.id.event_img);


            }


        }
    }
}
