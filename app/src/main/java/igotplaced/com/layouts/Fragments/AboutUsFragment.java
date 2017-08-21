package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import igotplaced.com.layouts.R;
public class AboutUsFragment extends Fragment implements View.OnClickListener {


    private Toolbar toolbar;
    private AppCompatEditText emailAddress;
    private Button subscribe;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("About us");
        // Inflate the layout for this fragment


        addressingView(view);
        addingListeners(view);
        return view;
    }

    private void addressingView(View view) {
        emailAddress=(AppCompatEditText) view.findViewById(R.id.editTextSubscribeEmail);
        subscribe=(Button) view.findViewById(R.id.subscribeButton);
    }

    private void addingListeners(View view) {

        subscribe.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        emailAddress.setText("");
        Toast.makeText(getContext(),"Thank you for your Subcription",Toast.LENGTH_LONG).show();
    }
}