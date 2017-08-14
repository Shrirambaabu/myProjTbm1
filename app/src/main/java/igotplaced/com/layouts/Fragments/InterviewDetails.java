package igotplaced.com.layouts.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import igotplaced.com.layouts.R;


public class InterviewDetails extends Fragment {
    private String id = null;


    public InterviewDetails() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_interview_details, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("iid");
            Log.e("iid", id);
        }
        // Inflate the layout for this fragment
        return view;
    }

}
