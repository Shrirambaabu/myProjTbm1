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
import igotplaced.com.layouts.Model.RecentFeeds;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

/**
 * Created by Admin on 5/23/2017.
 */

public class RecyclerAdapterMentorsHome  extends RecyclerView.Adapter<RecyclerAdapterMentorsHome.MyViewHolder> {

    private List<MentorsHome> mentorsHomeList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterMentorsHome(Context context, List<MentorsHome> mentorsHomeList) {

        this.context = context;
        this.mentorsHomeList = mentorsHomeList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.mentors_home, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MentorsHome mentorsList = mentorsHomeList.get(position);
        //Pass the values of feeds object to Views
        holder.mentor_name.setText(mentorsList.getMentorName());
        holder.mentor_profession.setText(mentorsList.getMentorProfession());
        holder.mentor_company.setText(mentorsList.getMentorCompany());
        holder.mentor_img.setImageUrl(mentorsList.getImageName(),NetworkController.getInstance(context).getImageLoader());
        holder.linked_in_logo.setImageUrl(mentorsList.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return mentorsHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mentor_name, mentor_profession,mentor_company;
        private NetworkImageView mentor_img,linked_in_logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            mentor_name = (TextView) itemView.findViewById(R.id.mentor_name);
            mentor_profession = (TextView) itemView.findViewById(R.id.mentor_profession);
            mentor_company = (TextView) itemView.findViewById(R.id.mentor_company);

            // Volley's NetworkImageView which will load Image from URL
            mentor_img = (NetworkImageView) itemView.findViewById(R.id.mentor_img);
            linked_in_logo = (NetworkImageView) itemView.findViewById(R.id.linked_in_logo);


        }
    }

}