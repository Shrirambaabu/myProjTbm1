package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.RecentlyGotPlacedHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

/**
 * Created by Admin on 5/24/2017.
 */

public class RecyclerAdapterRecentlyGotPlacedHome extends RecyclerView.Adapter<RecyclerAdapterRecentlyGotPlacedHome.MyViewHolder> {

    private List<RecentlyGotPlacedHome> recentlyGotPlacedList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterRecentlyGotPlacedHome(Context context, List<RecentlyGotPlacedHome> recentlyGotPlacedList) {

        this.context = context;
        this.recentlyGotPlacedList = recentlyGotPlacedList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.recently_got_placed_home, parent, false);
        return new MyViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecentlyGotPlacedHome recentlyGotPlaced = recentlyGotPlacedList.get(position);
        //Pass the values of feeds object to Views
        holder.PersonName.setText(recentlyGotPlaced.getPersonName());
        holder.PlacedDetails.setText(recentlyGotPlaced.getPlacedDetails());
        holder.prof_img.setImageUrl(recentlyGotPlaced.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return recentlyGotPlacedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView PersonName, PlacedDetails;
        private NetworkImageView prof_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            PersonName = (TextView) itemView.findViewById(R.id.recently_placed_profile_name);
            PlacedDetails = (TextView) itemView.findViewById(R.id.recently_placed_time);


            // Volley's NetworkImageView which will load Image from URL
            prof_img = (NetworkImageView) itemView.findViewById(R.id.recently_placed_profile_img);



        }
    }

}