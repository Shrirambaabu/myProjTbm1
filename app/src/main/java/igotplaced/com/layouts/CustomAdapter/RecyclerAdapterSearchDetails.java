package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.Model.SearchResultsModel;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 21-Aug-17.
 */

public class RecyclerAdapterSearchDetails extends RecyclerView.Adapter<RecyclerAdapterSearchDetails.MyViewHolder> {

    private List<SearchResultsModel> searchResultsModelList;
    private Context context;
    private LayoutInflater inflater;
    private final int post = 0, interview = 1, events = 2, questions = 3;

    public RecyclerAdapterSearchDetails(Context context, List<SearchResultsModel> searchResultsModelList) {

        this.context = context;
        this.searchResultsModelList = searchResultsModelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == post) {
            View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
            return new MyViewHolder(rootView);
        } else if (viewType == interview) {
            View rootView = inflater.inflate(R.layout.card_view_interview, parent, false);
            return new MyViewHolder(rootView);
        } else if (viewType == events) {
            View rootView = inflater.inflate(R.layout.card_view_event, parent, false);
            return new MyViewHolder(rootView);
        } else if (viewType == questions) {
            View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
            return new MyViewHolder(rootView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        SearchResultsModel searchResultsModel = searchResultsModelList.get(position);
        if (itemType == post) {

            ((MyViewHolder.postViewHolder)holder).post.setText(searchResultsModel.getPostMessage());
            ((MyViewHolder.postViewHolder)holder).postIndustry.setText(searchResultsModel.getPostIndustry()+ " "+searchResultsModel.getPostCompany());
            ((MyViewHolder.postViewHolder)holder).postProfileName.setText(searchResultsModel.getPostUserName());
            ((MyViewHolder.postViewHolder)holder).postTime.setText(searchResultsModel.getPostDate());
            ((MyViewHolder.postViewHolder)holder).postImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getPostUserImage(), NetworkController.getInstance(context).getImageLoader());


        }else if (itemType==interview){

            ((MyViewHolder.interviewViewHolder)holder).interview.setText(searchResultsModel.getInterviewMessage());
            ((MyViewHolder.interviewViewHolder)holder).interviewIndustry.setText(searchResultsModel.getInterviewIndustry()+ " "+ searchResultsModel.getInterviewCompany());
            ((MyViewHolder.interviewViewHolder)holder).interviewProfileName.setText(searchResultsModel.getInterviewProfileName());
            ((MyViewHolder.interviewViewHolder)holder).interviewTime.setText(searchResultsModel.getInterviewProfileDate());
            ((MyViewHolder.interviewViewHolder)holder).interviewImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getInterviewProfileImage(), NetworkController.getInstance(context).getImageLoader());



        }else if (itemType==events){

            ((MyViewHolder.eventsViewHolder)holder).eventCaption.setText(searchResultsModel.getEventName());
            ((MyViewHolder.eventsViewHolder)holder).eventDesignation.setText(searchResultsModel.getEventType());
            ((MyViewHolder.eventsViewHolder)holder).eventVenue.setText(searchResultsModel.getEventLocation());
            ((MyViewHolder.eventsViewHolder)holder).eventDate.setText(searchResultsModel.getDateTime());
            ((MyViewHolder.eventsViewHolder)holder).eventRegistered.setText(searchResultsModel.getCount());
            ((MyViewHolder.eventsViewHolder)holder).eventStatus.setText(searchResultsModel.getEvent());
            ((MyViewHolder.eventsViewHolder)holder).event.setText(searchResultsModel.getNotes());
            ((MyViewHolder.eventsViewHolder)holder).event_industry.setText(searchResultsModel.getEventIndustry()+ "  " +searchResultsModel.getEventCompany());
            ((MyViewHolder.eventsViewHolder)holder).event_profile_name.setText(searchResultsModel.getEventUserName());
            ((MyViewHolder.eventsViewHolder)holder).event_time.setText(searchResultsModel.getEventDate());
            ((MyViewHolder.eventsViewHolder)holder).event_img.setImageUrl(Utils.BaseImageUri + searchResultsModel.getEventUserImage(), NetworkController.getInstance(context).getImageLoader());



        }else if (itemType==questions){

            ((MyViewHolder.questionsViewHolder)holder).questions.setText(searchResultsModel.getQuestionMessage());
            ((MyViewHolder.questionsViewHolder)holder).questionsIndustry.setText(searchResultsModel.getQuestionIndustry()+ " " +searchResultsModel.getQuestionIndustry());
            ((MyViewHolder.questionsViewHolder)holder).questionsProfileName.setText(searchResultsModel.getQuestionUserName());
            ((MyViewHolder.questionsViewHolder)holder).questionsTime.setText(searchResultsModel.getQuestionDate());
            ((MyViewHolder.questionsViewHolder)holder).questionsImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getQuestionUserImage(), NetworkController.getInstance(context).getImageLoader());

        }

    }

    @Override
    public int getItemCount() {
        return searchResultsModelList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }

        class postViewHolder extends MyViewHolder implements View.OnClickListener {
            private TextView post, postIndustry, postProfileName, postTime,viewMore;

            private NetworkImageView postImage, userImage;
            private ItemClickListener itemClickListener;


            public postViewHolder(View itemView) {
                super(itemView);
                post = (TextView) itemView.findViewById(R.id.post);
                postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
                postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
                postTime = (TextView) itemView.findViewById(R.id.post_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

                // postTime = (TextView) itemView.findViewById(R.id.post_time);
                // Volley's NetworkImageView which will load Image from URL
                postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);
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

        class interviewViewHolder extends MyViewHolder implements View.OnClickListener {
            private TextView interview, interviewIndustry, interviewProfileName, interviewTime,viewMore;
            private NetworkImageView interviewImage, userImage;
            private ItemClickListener itemClickListener;


            public interviewViewHolder(View itemView) {
                super(itemView);

                interview = (TextView) itemView.findViewById(R.id.interview);
                interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
                interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
                interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

                // Volley's NetworkImageView which will load Image from URL
                interviewImage = (NetworkImageView) itemView.findViewById(R.id.interview_img);
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

        class eventsViewHolder extends MyViewHolder implements View.OnClickListener {
            private TextView eventCaption, eventDesignation, eventVenue, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time, viewMore;
            private NetworkImageView event_img, userImage;
            private ItemClickListener itemClickListener;

            public eventsViewHolder(View itemView) {
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
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

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


        class questionsViewHolder extends MyViewHolder implements View.OnClickListener {

            private TextView questions, questionsIndustry, questionsProfileName, questionsTime,viewMore;
            private NetworkImageView questionsImage, comment_profile_img;
            private ItemClickListener itemClickListener;


            public questionsViewHolder(View itemView) {
                super(itemView);

                questions = (TextView) itemView.findViewById(R.id.questions);
                questionsIndustry = (TextView) itemView.findViewById(R.id.questions_industry);
                questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
                questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);

                // Volley's NetworkImageView which will load Image from URL
                questionsImage = (NetworkImageView) itemView.findViewById(R.id.questions_img);
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