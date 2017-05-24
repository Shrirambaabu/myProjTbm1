package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.MentorsHome;
import igotplaced.com.layouts.Model.Testimonials;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

/**
 * Created by Admin on 5/24/2017.
 */

public class RecyclerAdapterTestmonials extends RecyclerView.Adapter<RecyclerAdapterTestmonials.MyViewHolder> {

    private List<Testimonials> testimonialsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterTestmonials(Context context, List<Testimonials> testimonialsList) {

        this.context = context;
        this.testimonialsList = testimonialsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.testimonials, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Testimonials testimonials = testimonialsList.get(position);
        //Pass the values of feeds object to Views
        holder.profileMessage.setText(testimonials.getProfileMessage());
        holder.profileName.setText(testimonials.getProfileName());
        holder.profileCollege.setText(testimonials.getProfileCollege());
        holder.testimonial_profile_img.setImageUrl(testimonials.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return testimonialsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView profileMessage, profileName,profileCollege;
        private NetworkImageView testimonial_profile_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            profileMessage = (TextView) itemView.findViewById(R.id.testimonial);
            profileName = (TextView) itemView.findViewById(R.id.testimonial_profile_name);
            profileCollege = (TextView) itemView.findViewById(R.id.testimonial_profile_college);

            // Volley's NetworkImageView which will load Image from URL
            testimonial_profile_img = (NetworkImageView) itemView.findViewById(R.id.testimonial_profile_img);


        }
    }

}