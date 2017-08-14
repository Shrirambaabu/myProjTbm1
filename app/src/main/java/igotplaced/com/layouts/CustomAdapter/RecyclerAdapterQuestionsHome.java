package igotplaced.com.layouts.CustomAdapter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.Model.Questions;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

/**
 * Created by Admin on 5/31/2017.
 */

public class RecyclerAdapterQuestionsHome extends RecyclerView.Adapter<RecyclerAdapterQuestionsHome.MyViewHolder> {

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/questionsComments";
    private String questionsId, postedQuestionUserId, userPostedComment;

    private List<Questions> questionsList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterQuestionsHome(Context context, List<Questions> questionsList) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        this.context = context;
        this.questionsList = questionsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_questions, parent, false);
        return new RecyclerAdapterQuestionsHome.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Questions questions = questionsList.get(position);
        questionsId = questions.getQuestionId();
        postedQuestionUserId = questions.getQuestionUserId();
        //Pass the values of feeds object to Views
        holder.questions.setText(questions.getQuestions());
        holder.questionsIndustry.setText(questions.getQuestionsIndustry());
        holder.questionsProfileName.setText(questions.getQuestionsProfileName());
        holder.questionsTime.setText(questions.getQuestionsTime());
        //      holder.comment_profile_img.setImageUrl(Utils.BaseImageUri + questions.getCommentProfileImage(), NetworkController.getInstance(context).getImageLoader());
        holder.questionsImage.setImageUrl(Utils.BaseImageUri + questions.getQuestionsImage(), NetworkController.getInstance(context).getImageLoader());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.userComment.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Enter the Comment", Toast.LENGTH_SHORT).show();
                } else {
                    userPostedComment = holder.userComment.getText().toString();
                    insertUserComment();
                    Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show();
                }
                holder.userComment.setText("");

            }
        });

    }

    private void insertUserComment() {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue((Activity) context, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("qid", questionsId);
                parameters.put("ques_createrid", postedQuestionUserId);
                parameters.put("user_id", userId);
                parameters.put("comments", userPostedComment);
                parameters.put("created_uname", userName);

                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questions, questionsIndustry, questionsProfileName, questionsTime;
        private NetworkImageView questionsImage, comment_profile_img;
        private ImageView comment;
        private EditText userComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            questions = (TextView) itemView.findViewById(R.id.questions);
            questionsIndustry = (TextView) itemView.findViewById(R.id.questions_industry);
            questionsProfileName = (TextView) itemView.findViewById(R.id.questions_profile_name);
            questionsTime = (TextView) itemView.findViewById(R.id.questions_time);
            comment = (ImageView) itemView.findViewById(R.id.send_comment);
            userComment = (EditText) itemView.findViewById(R.id.user_comment);

            // Volley's NetworkImageView which will load Image from URL
            questionsImage = (NetworkImageView) itemView.findViewById(R.id.questions_img);
            //      comment_profile_img = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


}
