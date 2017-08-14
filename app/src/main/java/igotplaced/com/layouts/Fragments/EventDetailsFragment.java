package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import igotplaced.com.layouts.R;


public class EventDetailsFragment extends Fragment {
    private String id = null;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("eid");
            Log.e("eid", id);
        }
        // Inflate the layout for this fragment
        return view;
    }

}
