package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.RecentFeedsHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Ashith VL on 5/22/2017.
 */

public class RecyclerAdapterRecentFeedsHome extends RecyclerView.Adapter<RecyclerAdapterRecentFeedsHome.MyViewHolder> {

    private List<RecentFeedsHome> feedsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterRecentFeedsHome(Context context, List<RecentFeedsHome> feedsList) {

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
        RecentFeedsHome recentFeedsHome = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.feed.setText(recentFeedsHome.getQuestion());
        holder.feed_industry.setText(recentFeedsHome.getIndustryName());
        holder.feed_profile_name.setText(recentFeedsHome.getName());
        holder.feed_time.setText(recentFeedsHome.getModifiedBy());
        holder.feed_profile_img.setImageUrl(Utils.BaseImageUri+recentFeedsHome.getImageName(), NetworkController.getInstance(context).getImageLoader());

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