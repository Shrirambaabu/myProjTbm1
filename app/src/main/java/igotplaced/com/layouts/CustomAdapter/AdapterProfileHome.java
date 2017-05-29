package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;


public class AdapterProfileHome  extends RecyclerView.Adapter<AdapterProfileHome.MyViewHolder> {

    private List<ProfileHome> profileHomeList;
    private Context context;
    private LayoutInflater inflater;

    public AdapterProfileHome(Context context, List<ProfileHome> profileList) {

        this.context = context;
        this.profileHomeList = profileList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_notification, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProfileHome profView = profileHomeList.get(position);
        //Pass the values of feeds object to Views
        holder.profileName.setText(profView.getProfileName());
        holder.departmentName.setText(profView.getDepartmentName());
        holder.collegeName.setText(profView.getCollegeName());
        holder.notify_img.setImageUrl(Utils.BaseImageUri+profView.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return profileHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView profileName, departmentName,collegeName;
        private NetworkImageView notify_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            profileName = (TextView) itemView.findViewById(R.id.user_profile_name);
            departmentName = (TextView) itemView.findViewById(R.id.user_profile_department);
            collegeName = (TextView) itemView.findViewById(R.id.user_profile_college);
            // Volley's NetworkImageView which will load Image from URL
            notify_img = (NetworkImageView) itemView.findViewById(R.id.user_profile_photo);


        }
    }

}