package igotplaced.com.layouts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.RecentFeeds;
import igotplaced.com.layouts.Utils.NetworkController;

/**
 * Created by Ashith VL on 5/22/2017.
 */

public class RecyclerAdapterRecentFeeds  extends RecyclerView.Adapter<RecyclerAdapterRecentFeeds.MyViewHolder> {

    private List<RecentFeeds> feedsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterRecentFeeds(Context context, List<RecentFeeds> feedsList) {

        this.context = context;
        this.feedsList = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.recent_feeds_home, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecentFeeds recentFeeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.feed.setText(recentFeeds.getQuestion());
        holder.feed_industry.setText(recentFeeds.getIndustryName());
        holder.feed_profile_name.setText(recentFeeds.getName());
        holder.feed_time.setText(recentFeeds.getModifiedBy());
        holder.feed_profile_img.setImageUrl(recentFeeds.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return feedsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView feed, feed_industry,feed_profile_name,feed_time;
        private NetworkImageView feed_img,feed_profile_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            feed = (TextView) itemView.findViewById(R.id.feed);
            feed_industry = (TextView) itemView.findViewById(R.id.feed_industry);
            feed_profile_name = (TextView) itemView.findViewById(R.id.feed_profile_name);
            feed_time = (TextView) itemView.findViewById(R.id.feed_time);
            // Volley's NetworkImageView which will load Image from URL
            feed_img = (NetworkImageView) itemView.findViewById(R.id.feed_img);
            feed_profile_img = (NetworkImageView) itemView.findViewById(R.id.feed_profile_img);


        }
    }

}