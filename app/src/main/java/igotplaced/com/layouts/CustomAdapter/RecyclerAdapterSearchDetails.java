package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Model.SearchResultsModel;
import igotplaced.com.layouts.ProfileEventDetails;
import igotplaced.com.layouts.ProfileInterviewDetailsActivity;
import igotplaced.com.layouts.ProfilePostDetailsActivity;
import igotplaced.com.layouts.ProfileQuestionsDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 21-Aug-17.
 */

public class RecyclerAdapterSearchDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchResultsModel> searchResultsModelList;
    ArrayList<Integer> list;
    private Context context;
    private LayoutInflater inflater;
    private final int post = 0, interview = 1, events = 2, questions = 3;

    public RecyclerAdapterSearchDetails(Context context, List<SearchResultsModel> searchResultsModelList, ArrayList<Integer> list) {

        this.list = list;
        this.context = context;
        this.searchResultsModelList = searchResultsModelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e("ListSize", "" + list.size());
    }


    @Override
    public int getItemViewType(int position) {
        Log.e("ListPos", "" + list.get(position));
        if (list.get(position) == 0) {
            return post;
        } else if (list.get(position) == 1) {
            return interview;
        } else if (list.get(position) == 2) {
            return events;
        } else  if (list.get(position) == 3){
            return questions;
        }else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e("ViewType", "" + viewType);
        if (viewType == post) {
            View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
            PostViewHolder holder = new PostViewHolder(rootView);
            return holder;
        } else if (viewType == interview) {
            View rootView = inflater.inflate(R.layout.card_view_interview, parent, false);
            InterviewViewHolder holder = new InterviewViewHolder(rootView);
            return holder;
        } else if (viewType == events) {
            View rootView = inflater.inflate(R.layout.card_view_event, parent, false);
            EventsViewHolder holder = new EventsViewHolder(rootView);
            return holder;
        } else if (viewType == questions) {
            View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
            QuestionsViewHolder holder = new QuestionsViewHolder(rootView);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //  final int itemType = getItemViewType(position);
        Integer integer = list.get(position);
        SearchResultsModel searchResultsModel = searchResultsModelList.get(position);
        Log.e("IndustryInterview",""+searchResultsModel.getCompany());
        if (integer != null) {

            if (integer==0) {

                ((PostViewHolder) holder).post.setText(searchResultsModel.getMessage());
                ((PostViewHolder) holder).postIndustry.setText("#"+searchResultsModel.getIndustry());
                ((PostViewHolder) holder).postProfileName.setText(searchResultsModel.getUserName());
                ((PostViewHolder) holder).postTime.setText(searchResultsModel.getCreatedDate());
                ((PostViewHolder) holder).postCompany.setText("#"+searchResultsModel.getCompany());
                ((PostViewHolder) holder).postImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getUserImage(), NetworkController.getInstance(context).getImageLoader());
                ((PostViewHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Log.e("Item Click post", "" + searchResultsModelList.get(position).getId());
                        Intent profileDetails=new Intent(context, ProfilePostDetailsActivity.class);

                        profileDetails.putExtra("pid", searchResultsModelList.get(position).getId());
                        profileDetails.putExtra("created_uname", searchResultsModelList.get(position).getUserName());
                        profileDetails.putExtra("created_by", searchResultsModelList.get(position).getCreatedDate());
                        profileDetails.putExtra("post", searchResultsModelList.get(position).getMessage());
                        profileDetails.putExtra("postImage", searchResultsModelList.get(position).getUserImage());
                        profileDetails.putExtra("postIndustry", searchResultsModelList.get(position).getIndustry());
                        profileDetails.putExtra("post_createdid", searchResultsModelList.get(position).getUserId());
                        profileDetails.putExtra("postCompany", searchResultsModelList.get(position).getCompany());
                        profileDetails.putExtra("companyId", searchResultsModelList.get(position).getCompanyId());
                        context.startActivity(profileDetails);
                    }
                });

            } else if (integer==1) {

                ((InterviewViewHolder) holder).interview.setText(searchResultsModel.getMessage());
                ((InterviewViewHolder) holder).interviewIndustry.setText("#"+searchResultsModel.getIndustry());
                ((InterviewViewHolder) holder).interviewProfileName.setText(searchResultsModel.getUserName());
                ((InterviewViewHolder) holder).interviewCompany.setText("#"+searchResultsModel.getCompany());
                ((InterviewViewHolder) holder).interviewTime.setText(searchResultsModel.getCreatedDate());
                ((InterviewViewHolder) holder).interviewImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getUserImage(), NetworkController.getInstance(context).getImageLoader());

                ((InterviewViewHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Log.e("Item Click Interview", "" + searchResultsModelList.get(position).getId());

                        Intent profileInterview=new Intent(context, ProfileInterviewDetailsActivity.class);

                        profileInterview.putExtra("iid", searchResultsModelList.get(position).getId());
                        profileInterview.putExtra("created_uname", searchResultsModelList.get(position).getUserName());
                        profileInterview.putExtra("created_by", searchResultsModelList.get(position).getCreatedDate());
                        profileInterview.putExtra("interview", searchResultsModelList.get(position).getMessage());
                        profileInterview.putExtra("interviewImage", searchResultsModelList.get(position).getUserImage());
                        profileInterview.putExtra("interviewIndustry", searchResultsModelList.get(position).getIndustry());
                        profileInterview.putExtra("interview_createdid", searchResultsModelList.get(position).getUserId());
                        profileInterview.putExtra("postCompany",searchResultsModelList.get(position).getCompany());
                        profileInterview.putExtra("companyId", searchResultsModelList.get(position).getCompanyId());
                        context.startActivity(profileInterview);

                    }
                });


            } else if (integer==2) {

                ((EventsViewHolder) holder).eventCaption.setText(searchResultsModel.getEventCaption());
                ((EventsViewHolder) holder).eventDesignation.setText(searchResultsModel.getEventType());
                ((EventsViewHolder) holder).eventVenue.setText(searchResultsModel.getEventLocation());
                ((EventsViewHolder) holder).eventDate.setText(searchResultsModel.getEventDateTime());
                ((EventsViewHolder) holder).eventRegistered.setText(searchResultsModel.getEventCount());
                ((EventsViewHolder) holder).eventStatus.setText(searchResultsModel.getEventStatus());
                ((EventsViewHolder) holder).event.setText(searchResultsModel.getMessage());
                ((EventsViewHolder) holder).eventCompany.setText("#"+searchResultsModel.getCompany());
                ((EventsViewHolder) holder).event_industry.setText("#"+searchResultsModel.getIndustry());
                ((EventsViewHolder) holder).event_profile_name.setText(searchResultsModel.getUserName());
                ((EventsViewHolder) holder).event_time.setText(searchResultsModel.getCreatedDate());
                ((EventsViewHolder) holder).event_img.setImageUrl(Utils.BaseImageUri + searchResultsModel.getUserImage(), NetworkController.getInstance(context).getImageLoader());

                ((EventsViewHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Log.e("Item Click Event", "" + searchResultsModelList.get(position).getId());
                        Intent profileEventDetails =new Intent(context,ProfileEventDetails.class);


                        profileEventDetails.putExtra("eid", searchResultsModelList.get(position).getId());
                        profileEventDetails.putExtra("ename", searchResultsModelList.get(position).getUserName());
                        profileEventDetails.putExtra("eTime", searchResultsModelList.get(position).getCreatedDate());
                        profileEventDetails.putExtra("eCaption", searchResultsModelList.get(position).getEventCaption());
                        profileEventDetails.putExtra("eDesign", searchResultsModelList.get(position).getEventType());
                        profileEventDetails.putExtra("eVenue", searchResultsModelList.get(position).getEventLocation());
                        profileEventDetails.putExtra("eDate", searchResultsModelList.get(position).getEventDateTime());
                        profileEventDetails.putExtra("eReg", searchResultsModelList.get(position).getEventCount());
                        profileEventDetails.putExtra("eStatus", searchResultsModelList.get(position).getEventStatus());
                        profileEventDetails.putExtra("eEvnt", searchResultsModelList.get(position).getMessage());
                        profileEventDetails.putExtra("eIndustry", searchResultsModelList.get(position).getIndustry());
                        profileEventDetails.putExtra("eImage", searchResultsModelList.get(position).getUserImage());
                        profileEventDetails.putExtra("eUserId", searchResultsModelList.get(position).getUserId());

                        context.startActivity(profileEventDetails);
                    }
                });


            } else if (integer==3) {

                ((QuestionsViewHolder) holder).questions.setText(searchResultsModel.getMessage());
                ((QuestionsViewHolder) holder).questionsIndustry.setText("#"+searchResultsModel.getIndustry() );
                ((QuestionsViewHolder) holder).questionsCompany.setText("#"+searchResultsModel.getCompany() );
                ((QuestionsViewHolder) holder).questionsProfileName.setText(searchResultsModel.getUserName());
                ((QuestionsViewHolder) holder).questionsTime.setText(searchResultsModel.getCreatedDate());
                ((QuestionsViewHolder) holder).questionsImage.setImageUrl(Utils.BaseImageUri + searchResultsModel.getUserImage(), NetworkController.getInstance(context).getImageLoader());

                ((QuestionsViewHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Log.e("Item Click question", "" + searchResultsModelList.get(position).getId());

                        Intent questionDetails=new Intent(context, ProfileQuestionsDetailsActivity.class);

                        questionDetails.putExtra("qid", searchResultsModelList.get(position).getId());
                        questionDetails.putExtra("created_uname", searchResultsModelList.get(position).getUserName());
                        questionDetails.putExtra("created_by", searchResultsModelList.get(position).getCreatedDate());
                        questionDetails.putExtra("question", searchResultsModelList.get(position).getMessage());
                        questionDetails.putExtra("postImage", searchResultsModelList.get(position).getUserImage());
                        questionDetails.putExtra("postIndustry", searchResultsModelList.get(position).getIndustry());
                        questionDetails.putExtra("post_createdid", searchResultsModelList.get(position).getUserId());
                        questionDetails.putExtra("postCompany",searchResultsModelList.get(position).getCompany());
                        questionDetails.putExtra("companyId", searchResultsModelList.get(position).getCompanyId());
                        context.startActivity(questionDetails);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return searchResultsModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView post, postIndustry, postProfileName, postTime, postCompany,viewMore;

        private NetworkImageView postImage, userImage;
        private ItemClickListener itemClickListener;


        public PostViewHolder(View itemView) {
            super(itemView);
            post = (TextView) itemView.findViewById(R.id.post);
            postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
            postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            viewMore = (TextView) itemView.findViewById(R.id.view_more);
            postCompany = (TextView) itemView.findViewById(R.id.post_company);

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

    class InterviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView interview, interviewIndustry, interviewProfileName, interviewTime,interviewCompany, viewMore;
        private NetworkImageView interviewImage, userImage;
        private ItemClickListener itemClickListener;


        public InterviewViewHolder(View itemView) {
            super(itemView);

            interview = (TextView) itemView.findViewById(R.id.interview);
            interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
            interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
            interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
            viewMore = (TextView) itemView.findViewById(R.id.view_more);
            interviewCompany = (TextView) itemView.findViewById(R.id.interview_company);
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

    class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventCaption, eventDesignation, eventVenue,eventCompany, eventDate, eventRegistered, eventStatus, event, event_industry, event_profile_name, event_time, viewMore;
        private NetworkImageView event_img, userImage;
        private ItemClickListener itemClickListener;

        public EventsViewHolder(View itemView) {
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


    class QuestionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questions, questionsIndustry, questionsProfileName, questionsTime,questionsCompany, viewMore;
        private NetworkImageView questionsImage, comment_profile_img;
        private ItemClickListener itemClickListener;


        public QuestionsViewHolder(View itemView) {
            super(itemView);

            questions = (TextView) itemView.findViewById(R.id.questions);
            questionsIndustry = (TextView) itemView.findViewById(R.id.questions_industry);
            questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
            questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
            viewMore = (TextView) itemView.findViewById(R.id.view_more);
            questionsCompany = (TextView) itemView.findViewById(R.id.questions_company);
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