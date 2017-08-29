package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Fragments.NotificationFragment;
import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.QuestionsPopUpActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;


public class RecyclerAdapterNotification  extends RecyclerView.Adapter<RecyclerAdapterNotification.MyViewHolder> {

    private List<NotificationView> notificationViewList;
    private Context context;
    private LayoutInflater inflater;



    public RecyclerAdapterNotification(Context context, List<NotificationView> notificationList) {

        this.context = context;
        this.notificationViewList = notificationList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_notification, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NotificationView notifyView = notificationViewList.get(position);
        //Pass the values of feeds object to Views
        holder.createdBy.setText(notifyView.getCreatedBy());
        holder.notificationPost.setText(notifyView.getNotificationPost());
        holder.notify_img.setImageUrl(Utils.BaseImageUri+notifyView.getImageName(), NetworkController.getInstance(context).getImageLoader());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.e("tag", "click" + notificationViewList.get(position).getNotifyId());

                Intent profileDetails=new Intent(context, QuestionsPopUpActivity.class);
                profileDetails.putExtra("qid", notificationViewList.get(position).getNotifyId());

                profileDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                context.startActivity(profileDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationViewList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView createdBy, notificationPost;
        private NetworkImageView notify_img;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            createdBy = (TextView) itemView.findViewById(R.id.notify_time);
            notificationPost = (TextView) itemView.findViewById(R.id.notifyMsg);
            // Volley's NetworkImageView which will load Image from URL
            notify_img = (NetworkImageView) itemView.findViewById(R.id.notify_img);
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