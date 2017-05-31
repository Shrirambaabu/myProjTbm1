package igotplaced.com.layouts.CustomAdapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.Questions;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Admin on 5/31/2017.
 */

    public class RecyclerAdapterQustionsHome extends RecyclerView.Adapter<RecyclerAdapterQustionsHome.MyViewHolder> {

    private List<Questions> questionsList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterQustionsHome(Context context, List<Questions> questionsList) {

        this.context = context;
        this.questionsList = questionsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
        return new RecyclerAdapterQustionsHome.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Questions questions = questionsList.get(position);
        //Pass the values of feeds object to Views
        holder.questions.setText(questions.getQuestions());
        holder.questionsIndustry.setText(questions.getQuestionsIndustry());
        holder.questionsProfileName.setText(questions.getCommentProfileImage());
        holder.questionsTime.setText(questions.getQuestionsTime());
        holder.userQustionComments.setText(questions.getQuestionsTime());
        holder.comment_profile_img.setImageUrl(Utils.BaseImageUri + questions.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
        holder.questionsImage.setImageUrl(Utils.BaseImageUri + questions.getQuestionsImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questions, questionsIndustry, questionsProfileName, questionsTime,userQustionComments;
        private NetworkImageView questionsImage, comment_profile_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            questions = (TextView) itemView.findViewById(R.id.questions);
            questionsIndustry = (TextView) itemView.findViewById(R.id.post_industry);
            questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
            questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
            userQustionComments = (TextView) itemView.findViewById(R.id.user_comment);

            // Volley's NetworkImageView which will load Image from URL
            questionsImage = (NetworkImageView) itemView.findViewById(R.id.questions_img);
            comment_profile_img = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


}
