package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Admin on 5/30/2017.
 */

public class RecyclerAdapterEventHome extends RecyclerView.Adapter<RecyclerAdapterEventHome.MyViewHolder> {



    private List<Events> eventsList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterEventHome(Context context, List<Events> eventsList) {

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
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Events events= eventsList.get(position);
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

        holder.userImage.setImageUrl(Utils.BaseImageUri + events.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
        holder.event_img.setImageUrl(Utils.BaseImageUri + events.getEventImage(), NetworkController.getInstance(context).getImageLoader());


    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView eventCaption, eventDesignation, eventVenue, eventDate,eventRegistered,eventStatus,event,event_industry,event_profile_name,event_time;
        private NetworkImageView event_img,userImage;

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

            event_time = (TextView) itemView.findViewById(R.id.event_time);
            // Volley's NetworkImageView which will load Image from URL
            event_img = (NetworkImageView) itemView.findViewById(R.id.event_img);
            userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }



}
