package igotplaced.com.layouts.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;

/**
 * Created by Shriram on 17-Aug-17.
 */

public class RecyclerAdapterPostDetails extends RecyclerView.Adapter<RecyclerAdapterPostDetails.MyViewHolder> {

    private List<Post> postList;
    private Context context;
    private LayoutInflater inflater;
    private String commentId, userId, postedUserId;

    public RecyclerAdapterPostDetails(Context context, List<Post> postList) {

        this.context = context;
        this.postList = postList;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Id, null);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.post_comments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Post post = postList.get(position);
        commentId = postList.get(position).getPostId();
        postedUserId = postList.get(position).getPostedUserId();

        holder.commentedPost.setText(post.getCommentedMessage());
        holder.postImage.setImageUrl(Utils.BaseImageUri + post.getComentedUserImage(), NetworkController.getInstance(context).getImageLoader());

        if (postedUserId.equals(userId)) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);
                    otherProfileDetails.putExtra("post_createdid", postList.get(position).getPostedUserId());
                    context.startActivity(otherProfileDetails);
                }
            });
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "click" + postList.get(position).getPostId());


                new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete")
                        .setMessage("Are you sure you want to Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                deleteUserComment();

                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }

    private void deleteUserComment() {


        StringRequest request = new StringRequest(Request.Method.POST, BaseUri + "/home/deletePostComment/" + commentId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                notifyDataSetChanged();

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
                parameters.put("user_id", commentId);


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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView postImage;
        private TextView commentedPost;
        private ImageView delete;


        public MyViewHolder(View itemView) {
            super(itemView);

            postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);
            commentedPost = (TextView) itemView.findViewById(R.id.commented_post);
            delete = (ImageView) itemView.findViewById(R.id.delete);

        }


    }
}