package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 17-Aug-17.
 */

public class RecyclerAdapterInterviewDetails extends RecyclerView.Adapter<RecyclerAdapterInterviewDetails.MyViewHolder>{


    private List<Interview> interviewList;
    private Context context;
    private LayoutInflater inflater;


    public RecyclerAdapterInterviewDetails(Context context, List<Interview> interviewList) {
        this.context = context;
        this.interviewList = interviewList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.post_comments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Interview interview = interviewList.get(position);
        holder.commentedPost.setText(interview.getCommentMessage());
        holder.postImage.setImageUrl(Utils.BaseImageUri + interview.getComentUserImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return interviewList.size();
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