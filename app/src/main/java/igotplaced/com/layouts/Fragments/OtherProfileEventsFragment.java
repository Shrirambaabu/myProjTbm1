package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import igotplaced.com.layouts.ProfileEventDetails;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class OtherProfileEventsFragment extends Fragment {

    private Context context;
    private RequestQueue queue;
    private List<Events> eventsList = new ArrayList<Events>();
    private RecyclerAdapterOtherProfileEvent recyclerAdapterOtherProfileEvent;
    private String userId;

    private LinearLayoutManager mLayoutManager;

    public OtherProfileEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile_events, container, false);

        context = getActivity().getApplicationContext();
        mLayoutManager = new LinearLayoutManager(context);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            userId = bundle.getString("otherId");
        }


        eventRecyclerView(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void eventRecyclerView(View view) {

        RecyclerView event_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_events);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterOtherProfileEvent = new RecyclerAdapterOtherProfileEvent(context, eventsList);

        //setting fixed size
        event_view.setHasFixedSize(true);
        //setting horizontal layout
        event_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = (LinearLayoutManager) event_view.getLayoutManager();
        //setting RecyclerView adapter
        event_view.setAdapter(recyclerAdapterOtherProfileEvent);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        makeJsonArrayRequestEventHome();


    }

    private void makeJsonArrayRequestEventHome() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/profileEvent/" + userId, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        Events events = new Events(obj.getString("eid"), obj.getString("created_user"), obj.getString("eventname"), obj.getString("eventtype"), obj.getString("location"), obj.getString("datetime"), obj.getString("count"), obj.getString("event"), obj.getString("notes"), obj.getString("Industry"), obj.getString("eventimgname"), obj.getString("created_uname"), obj.getString("created_by"), obj.getString("eventimgname"), obj.getString("created_uname"), obj.getString("companyname"));
                        // adding movie to blogHomeList array
                        eventsList.add(events);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterOtherProfileEvent.notifyDataSetChanged();
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


    private class RecyclerAdapterOtherProfileEvent extends RecyclerView.Adapter<RecyclerAdapterOtherProfileEvent.MyViewHolder> {


        private List<Events> eventsList;
        private Context context;
        private LayoutInflater inflater;


        public RecyclerAdapterOtherProfileEvent(Context context, List<Events> eventsList) {

            this.context = context;
            this.eventsList = eventsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public RecyclerAdapterOtherProfileEvent.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_event, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapterOtherProfileEvent.MyViewHolder holder, final int position) {

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
            holder.event_industry.setText(events.getEventIndustry());
            holder.event_profile_name.setText(events.getEventProfileName());

            holder.event_time.setText(events.getEventTime());

            //  holder.userImage.setImageUrl(Utils.BaseImageUri + events.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
            holder.event_img.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(context).getImageLoader());

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
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

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView eventCaption, eventDesignation, eventVenue, eventCompany, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time;
            private NetworkImageView event_img, userImage;
            private ItemClickListener itemClickListener;

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
                event_time = (TextView) itemView.findViewById(R.id.event_time);
                // Volley's NetworkImageView which will load Image from URL
                event_img = (NetworkImageView) itemView.findViewById(R.id.event_img);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                this.itemClickListener.onItemClick(v, getLayoutPosition());
            }

            void setItemClickListener(ItemClickListener ic) {
                this.itemClickListener = ic;
            }

        }
    }
}
