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
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

/**
 * Created by Shriram on 17-Aug-17.
 */

public class RecyclerAdapterQuestionDetails extends RecyclerView.Adapter<RecyclerAdapterQuestionDetails.MyViewHolder> {


    private List<Questions> questionsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterQuestionDetails(Context context, List<Questions> questionsList) {

        this.context = context;
        this.questionsList = questionsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public RecyclerAdapterQuestionDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.post_comments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterQuestionDetails.MyViewHolder holder, int position) {
        Questions questions = questionsList.get(position);

        holder.commentedPost.setText(questions.getCommentUserMessage());
        holder.postImage.setImageUrl(Utils.BaseImageUri + questions.getCommentUserImage(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
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