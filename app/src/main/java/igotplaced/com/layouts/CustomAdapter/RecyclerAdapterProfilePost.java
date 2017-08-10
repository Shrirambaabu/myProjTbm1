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

import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 09-Aug-17.
 */

public class RecyclerAdapterProfilePost extends RecyclerView.Adapter<RecyclerAdapterProfilePost.MyViewHolder> {


    private List<Post> postList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterProfilePost(Context context, List<Post> postList) {

        this.context = context;
        this.postList = postList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerAdapterProfilePost.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.card_view_post, parent, false);
        return new RecyclerAdapterProfilePost.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterProfilePost.MyViewHolder holder, int position) {

        Post post = postList.get(position);

        Log.e("Profile Post",""+postList);
        //Pass the values of feeds object to Views
        holder.post.setText(post.getPost());
        holder.postIndustry.setText(post.getPostIndustry());
        holder.postProfileName.setText(post.getPostProfileName());
        holder.postTime.setText(post.getPostTime());
        //  holder.userImage.setImageUrl(Utils.BaseImageUri + post.getUserImage(), NetworkController.getInstance(context).getImageLoader());
        holder.postImage.setImageUrl(Utils.BaseImageUri + post.getPostImage(), NetworkController.getInstance(context).getImageLoader());


    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView post, postIndustry, postProfileName, postTime;
        private NetworkImageView postImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            post = (TextView) itemView.findViewById(R.id.post);
            postIndustry = (TextView) itemView.findViewById(R.id.post_industry);
            postProfileName = (TextView) itemView.findViewById(R.id.post_profile_name);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            // Volley's NetworkImageView which will load Image from URL
            postImage = (NetworkImageView) itemView.findViewById(R.id.post_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}
