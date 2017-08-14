package igotplaced.com.layouts.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import igotplaced.com.layouts.LoginActivity;
import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

/**
 * Created by Ashith VL on 5/29/2017.
 */

public class RecyclerAdapterPostHome extends RecyclerView.Adapter<RecyclerAdapterPostHome.MyViewHolder> {

    private String userId = null, userName = null;
    private String URL = BaseUri + "/home/postComments";
    private String postId, postedUserId, userPostedComment;


    private List<Post> postList;
    private Context context;
    private LayoutInflater inflater;


    public RecyclerAdapterPostHome(Context context, List<Post> postList) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        this.context = context;
        this.postList = postList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
        return new RecyclerAdapterPostHome.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Post post = postList.get(position);
        postId = post.getPostId();
        postedUserId = post.getPostedUserId();
        //Pass the values of feeds object to Views
        holder.post.setText(post.getPost());
        holder.postIndustry.setText(post.getPostIndustry());
        holder.postProfileName.setText(post.getPostProfileName());
        holder.postTime.setText(post.getPostTime());
        //  holder.userImage.setImageUrl(Utils.BaseImageUri + post.getUserImage(), NetworkController.getInstance(context).getImageLoader());
        holder.postImage.setImageUrl(Utils.BaseImageUri + post.getPostImage(), NetworkController.getInstance(context).getImageLoader());
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
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.e("tag", "click" + postList.get(position).getPostId());
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
                parameters.put("pid", postId);
                parameters.put("post_createdid", postedUserId);
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
        return postList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView post, postIndustry, postProfileName, postTime;
        private ImageView comment;
        private EditText userComment;
        private NetworkImageView postImage, userImage;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            post = (TextView) itemView.findViewById(R.id.post);
            postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
            postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
            postTime = (TextView) itemView.findViewById(R.id.post_time);

            comment = (ImageView) itemView.findViewById(R.id.send_comment);
            userComment = (EditText) itemView.findViewById(R.id.user_comment);

            // postTime = (TextView) itemView.findViewById(R.id.post_time);
            // Volley's NetworkImageView which will load Image from URL
            postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);

            // userImage = (NetworkImageView) itemView.findViewById(R.id.comment_profile_img);

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
