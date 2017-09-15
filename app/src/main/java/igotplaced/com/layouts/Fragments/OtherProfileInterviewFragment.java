package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CompanyDetailsActivity;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfileInterviewDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.screenSize;


public class OtherProfileInterviewFragment extends Fragment {

    private Context context;
    private RequestQueue queue;
    private String userId;
    private List<Interview> interviewList = new ArrayList<Interview>();
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapterOtherProfileInterview recyclerAdapterOtherProfileInterview;

    public OtherProfileInterviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile_interview, container, false);
        context = getActivity().getApplicationContext();

       // mLayoutManager = new LinearLayoutManager(context);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            userId = bundle.getString("otherId");
        }

        Log.e("InterExId",""+userId);

        interviewRecyclerView(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void interviewRecyclerView(View view) {

        //mapping RecyclerView
        RecyclerView interview_view = (RecyclerView) view.findViewById(R.id.recycler_view_profile_interview);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterOtherProfileInterview = new RecyclerAdapterOtherProfileInterview(context, interviewList);

        //setting fixed size
        Log.e("ScreenSizeReecyvlr", "" + screenSize(getActivity()));
        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        interview_view.setHasFixedSize(true);
        //setting horizontal layout
        interview_view.setLayoutManager(mLayoutManager);
        interview_view.setAdapter(recyclerAdapterOtherProfileInterview);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        makeJsonArrayRequestInterviewHome();

    }

    private void makeJsonArrayRequestInterviewHome() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BaseUri + "/profileService/profileInterviewExperience/" + userId, null,  new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        Interview interview = new Interview(obj.getString("fid"), obj.getString("user_id"), obj.getString("feedback"), obj.getString("industryname"), obj.getString("interviewExperienceimgname"), obj.getString("username"), obj.getString("created_by"), obj.getString("interviewExperienceimgname"), obj.getString("username"),obj.getString("companyname"),obj.getString("company_id"));
                        // adding movie to blogHomeList array
                        interviewList.add(interview);


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterOtherProfileInterview.notifyDataSetChanged();
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());

            }
        });

        queue.add(jsonArrayRequest);

    }


    private class RecyclerAdapterOtherProfileInterview extends RecyclerView.Adapter<RecyclerAdapterOtherProfileInterview.MyViewHolder> {


        private List<Interview> interviewList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterOtherProfileInterview(Context context, List<Interview> interviewList) {

            this.context = context;
            this.interviewList = interviewList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public RecyclerAdapterOtherProfileInterview.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = inflater.inflate(R.layout.card_view_interview, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapterOtherProfileInterview.MyViewHolder holder, final int position) {
            Interview interview = interviewList.get(position);

            //Pass the values of feeds object to Views
            holder.interview.setText(interview.getInterview());
            holder.interviewIndustry.setText("#"+interview.getInterviewIndustry());
            if (interview.getInterviewCompany().equals("")){
                holder.interviewCompany.setText(interview.getInterviewCompany());
            }else{
                holder.interviewCompany.setText("#"+interview.getInterviewCompany());
            }
            holder.interviewProfileName.setText(interview.getInterviewProfileName());
            holder.interviewTime.setText(interview.getInterviewTime());
            //  holder.userImage.setImageUrl(Utils.BaseImageUri + interview.getUserImage(), NetworkController.getInstance(context).getImageLoader());
            holder.interviewImage.setImageUrl(Utils.BaseImageUri + interview.getInterviewImage(), NetworkController.getInstance(context).getImageLoader());

            if (!interviewList.get(position).getInterviewUserId().equals(userId)) {
                holder.interviewProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);

                        otherProfileDetails.putExtra("post_createdid", interviewList.get(position).getInterviewUserId());
                        otherProfileDetails.putExtra("created_uname", interviewList.get(position).getInterviewProfileName());
                        startActivity(otherProfileDetails);
                    }
                });
            }
            holder.interviewCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent companyDetails = new Intent(context, CompanyDetailsActivity.class);
                    companyDetails.putExtra("postCompany",  interviewList.get(position).getInterviewCompany());
                    companyDetails.putExtra("companyId",  interviewList.get(position).getCompanyId());
                    startActivity(companyDetails);
                }
            });



            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileInterview=new Intent(getContext(), ProfileInterviewDetailsActivity.class);

                    profileInterview.putExtra("iid", interviewList.get(position).getInterviewId());
                    profileInterview.putExtra("created_uname", interviewList.get(position).getInterviewProfileName());
                    profileInterview.putExtra("created_by", interviewList.get(position).getInterviewTime());
                    profileInterview.putExtra("interview", interviewList.get(position).getInterview());
                    profileInterview.putExtra("interviewImage", interviewList.get(position).getInterviewImage());
                    profileInterview.putExtra("interviewIndustry", interviewList.get(position).getInterviewIndustry());
                    profileInterview.putExtra("interview_createdid", interviewList.get(position).getInterviewUserId());
                    profileInterview.putExtra("postCompany", interviewList.get(position).getInterviewCompany());
                    profileInterview.putExtra("companyId", interviewList.get(position).getCompanyId());
                    startActivity(profileInterview);
                }
            });
        }

        @Override
        public int getItemCount() {
            return interviewList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder  {
            private TextView interview, interviewIndustry, interviewProfileName, interviewTime,interviewCompany, viewMore;
            private NetworkImageView interviewImage, userImage;
            private ItemClickListener itemClickListener;

            public MyViewHolder(View itemView) {
                super(itemView);

                interview = (TextView) itemView.findViewById(R.id.interview);
                interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
                interviewCompany = (TextView) itemView.findViewById(R.id.interview_company);
                interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
                interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);


                // Volley's NetworkImageView which will load Image from URL
                interviewImage = (NetworkImageView) itemView.findViewById(R.id.interview_img);


            }


        }
    }
}
