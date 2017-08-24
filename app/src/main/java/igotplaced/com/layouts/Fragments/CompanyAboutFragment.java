package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import igotplaced.com.layouts.AboutCompanyActivity;
import igotplaced.com.layouts.Model.Company;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class CompanyAboutFragment extends Fragment {

    private String userId;
    private TextView AboutText;
    private RequestQueue queue;
    public CompanyAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_about, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getString("otherId");
        }
        AboutText=(TextView) view.findViewById(R.id.company_details);

        queue = NetworkController.getInstance(getContext()).getRequestQueue();
        makeJsonRequest();
        AboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent companyAbout=new Intent(getContext(), AboutCompanyActivity.class);

                companyAbout.putExtra("companyDetails",""+AboutText.getText().toString());
                startActivity(companyAbout);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void makeJsonRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/companyDetails/" + userId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);


                        Company company = new Company(obj.getString("id"),obj.getString("companyname"),obj.getString("companywebsite"),obj.getString("companyImage"),obj.getString("aboutus"));
                        AboutText.setText(company.getCompanyAbout());
                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
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


}
