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
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 17-Aug-17.
 */

public class RecyclerAdapterPostDetails extends RecyclerView.Adapter<RecyclerAdapterPostDetails.MyViewHolder> {

    private List<Post> postList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterPostDetails(Context context, List<Post> postList) {

        this.context = context;
        this.postList = postList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.post_comments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.commentedPost.setText(post.getCommentedMessage());
        holder.postImage.setImageUrl(Utils.BaseImageUri + post.getComentedUserImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView postImage;
        private TextView commentedPost;

        public MyViewHolder(View itemView) {
            super(itemView);

            postImage=(NetworkImageView) itemView.findViewById(R.id.post_img);
            commentedPost=(TextView) itemView.findViewById(R.id.commented_post);
        }
    }
}