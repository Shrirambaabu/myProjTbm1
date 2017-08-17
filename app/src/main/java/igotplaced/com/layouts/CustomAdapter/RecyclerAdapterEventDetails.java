package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.Events;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 17-Aug-17.
 */

public  class RecyclerAdapterEventDetails extends RecyclerView.Adapter<RecyclerAdapterEventDetails.MyViewHolder>{

    private List<Events> eventsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterEventDetails(Context context, List<Events> eventsList) {

        this.context = context;
        this.eventsList = eventsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public RecyclerAdapterEventDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.post_comments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterEventDetails.MyViewHolder holder, int position) {

        Events events = eventsList.get(position);
        holder.commentedPost.setText(events.getEventCommentMessage());
        holder.postImage.setImageUrl(Utils.BaseImageUri + events.getEventCommentImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
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