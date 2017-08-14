package igotplaced.com.layouts.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

/**
 * Created by Admin on 5/30/2017.
 */

public class RecyclerAdapterEventHome extends RecyclerView.Adapter<RecyclerAdapterEventHome.MyViewHolder> {

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/eventsComments";
    private String eventsId, postedEventsUserId, userPostedComment;

    private List<Events> eventsList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

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
        return new RecyclerAdapterEventHome.MyViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Events events = eventsList.get(position);

        eventsId = events.getEventId();
        postedEventsUserId = events.getEventUserId();

        //Pass the values of feeds object to Views
        holder.eventCaption.setText(events.getEventCaption());
        holder.eventDesignation.setText(events.getEventDesignation());
        holder.eventVenue.setText(events.getEventVenue());
        holder.eventDate.setText(events.getEventDate());
        holder.eventRegistered.setText(events.getEventRegistered());

        holder.eventStatus.setText(events.getEventStatus());
        holder.event.setText(events.getEvent());
        holder.event_industry.setText(events.getEventIndustry());
        holder.event_profile_name.setText(events.getEventProfileName());

        holder.event_time.setText(events.getEventTime());

        //  holder.userImage.setImageUrl(Utils.BaseImageUri + events.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
        holder.event_img.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(context).getImageLoader());

        holder.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.userComment.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Enter the Comment", Toast.LENGTH_SHORT).show();
                } else {
                    userPostedComment = holder.userComment.getText().toString();
                    insertUserComment();
                    Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show();
                }
                holder.userComment.setText("");
            }
        });

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
                Utils.showDialogue((Activity) context, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("eid", eventsId);
                parameters.put("evt_createrid", postedEventsUserId);
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

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView eventCaption, eventDesignation, eventVenue, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time;
        private NetworkImageView event_img, userImage;
        private EditText userComment;
        private ImageView sendComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventCaption = (TextView) itemView.findViewById(R.id.eventCaption);
            eventDesignation = (TextView) itemView.findViewById(R.id.eventDesignation);
            eventVenue = (TextView) itemView.findViewById(R.id.eventVenue);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            eventRegistered = (TextView) itemView.findViewById(R.id.eventRegistered);


            eventStatus = (TextView) itemView.findViewById(R.id.eventStatus);
            event = (TextView) itemView.findViewById(R.id.event);
            event_industry = (TextView) itemView.findViewById(R.id.event_industry);
            event_profile_name = (TextView) itemView.findViewById(R.id.event_profile_name);
            userComment = (EditText) itemView.findViewById(R.id.user_comment);
            sendComment = (ImageView) itemView.findViewById(R.id.send_comment);

            event_time = (TextView) itemView.findViewById(R.id.event_time);
            // Volley's NetworkImageView which will load Image from URL
            event_img = (NetworkImageView) itemView.findViewById(R.id.event_img);
            // userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


}
