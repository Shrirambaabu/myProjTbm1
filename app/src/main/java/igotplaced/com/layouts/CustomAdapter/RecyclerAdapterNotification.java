package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Fragments.NotificationFragment;
import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;


public class RecyclerAdapterNotification  extends RecyclerView.Adapter<RecyclerAdapterNotification.MyViewHolder> {

    private List<NotificationView> notificationViewList;
    private Context context;
    private LayoutInflater inflater;

    private ClickListener clickListener;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationView notifyView = notificationViewList.get(position);
        //Pass the values of feeds object to Views
        holder.createdBy.setText(notifyView.getCreatedBy());
        holder.notificationPost.setText(notifyView.getNotificationPost());
        holder.notify_img.setImageUrl(Utils.BaseImageUri+notifyView.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return notificationViewList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView createdBy, notificationPost;
        private NetworkImageView notify_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            createdBy = (TextView) itemView.findViewById(R.id.notify_time);
            notificationPost = (TextView) itemView.findViewById(R.id.notifyMsg);
            // Volley's NetworkImageView which will load Image from URL
            notify_img = (NetworkImageView) itemView.findViewById(R.id.notify_img);


        }
    }

}