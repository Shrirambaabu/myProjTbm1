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

import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

/**
 * Created by Admin on 5/30/2017.
 */

public class RecyclerAdapterInterviewHome extends RecyclerView.Adapter<RecyclerAdapterInterviewHome.MyViewHolder> {

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/interviewComments";
    private String interviewId, postedinterviewId, userinterviewComment;


    private List<Interview> interviewList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterInterviewHome(Context context, List<Interview> interviewList) {


        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Interview interview = interviewList.get(position);

        interviewId=interview.getInterviewId();
        postedinterviewId=interview.getInterviewUserId();
        //Pass the values of feeds object to Views
        holder.interview.setText(interview.getInterview());
        holder.interviewIndustry.setText(interview.getInterviewIndustry());
        holder.interviewProfileName.setText(interview.getInterviewProfileName());
        holder.interviewTime.setText(interview.getInterviewTime());
      //  holder.userImage.setImageUrl(Utils.BaseImageUri + interview.getUserImage(), NetworkController.getInstance(context).getImageLoader());
        holder.interviewImage.setImageUrl(Utils.BaseImageUri + interview.getInterviewImage(), NetworkController.getInstance(context).getImageLoader());

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.userComment.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter the Comment", Toast.LENGTH_SHORT).show();
                }else {
                    userinterviewComment= holder.userComment.getText().toString();
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
                parameters.put("iid", interviewId);
                parameters.put("intex_createrid", postedinterviewId);
                parameters.put("user_id", userId);
                parameters.put("comments", userinterviewComment);
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
        return interviewList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView interview, interviewIndustry, interviewProfileName, interviewTime;
        private NetworkImageView interviewImage,userImage;
        private ImageView comment;
        private EditText userComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            interview = (TextView) itemView.findViewById(R.id.interview);
            interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
            interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
            interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
            comment = (ImageView) itemView.findViewById(R.id.send_comment);
            userComment = (EditText) itemView.findViewById(R.id.user_comment);

            // Volley's NetworkImageView which will load Image from URL
            interviewImage = (NetworkImageView) itemView.findViewById(R.id.interview_img);
          //  userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }



}
