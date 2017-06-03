package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Admin on 5/30/2017.
 */

public class RecyclerAdapterInterviewHome extends RecyclerView.Adapter<RecyclerAdapterInterviewHome.MyViewHolder> {

    private List<Interview> interviewList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterInterviewHome(Context context, List<Interview> interviewList) {

        this.context = context;
        this.interviewList = interviewList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_interview, parent, false);
        return new RecyclerAdapterInterviewHome.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Interview interview = interviewList.get(position);
        //Pass the values of feeds object to Views
        holder.interview.setText(interview.getInterview());
        holder.interviewIndustry.setText(interview.getInterviewIndustry());
        holder.interviewProfileName.setText(interview.getInterviewProfileName());
        holder.interviewTime.setText(interview.getInterviewTime());
      //  holder.userImage.setImageUrl(Utils.BaseImageUri + interview.getUserImage(), NetworkController.getInstance(context).getImageLoader());
        holder.interviewImage.setImageUrl(Utils.BaseImageUri + interview.getInterviewImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return interviewList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView interview, interviewIndustry, interviewProfileName, interviewTime;
        private NetworkImageView interviewImage,userImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            interview = (TextView) itemView.findViewById(R.id.interview);
            interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
            interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
            interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
            // Volley's NetworkImageView which will load Image from URL
            interviewImage = (NetworkImageView) itemView.findViewById(R.id.interview_img);
            userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }



}
