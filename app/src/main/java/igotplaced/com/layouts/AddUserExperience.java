package igotplaced.com.layouts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class AddUserExperience extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private EditText addUserData;
    private Button addBtn, cancelBtn;
    private String industrySpinnerOneValue = "", companySpinnerOneValue = "";
    private AppCompatSpinner industrySpinnerOne = null;
    private AppCompatSpinner companySpinnerOne = null;
    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter1;
    private List<String> industrySpinnerArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add_user_experience);


        //mapping to view object
        mapping();
        //settingListners
        settingListeners();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Utils.showDialogue(AddUserExperience.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(AddUserExperience.this);

        if (screenSize(AddUserExperience.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void settingCompanySpinner() {
        companyArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);
        companySpinnerOne.setAdapter(companyArrayAdapter1);
    }

    private void settingIndustrySpinner() {
        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapter);
    }

    private List<String> networkIndustrySpinnerArrayRequest() {
        industrySpinnerArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/industry", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                industrySpinnerArrayList.add("-- Select --");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        industrySpinnerArrayList.add(String.valueOf(response.get(i)));
                        spinnerArrayAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(AddUserExperience.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(AddUserExperience.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private void mapping() {
        addUserData = (EditText) findViewById(R.id.getUserData);
        addBtn = (Button) findViewById(R.id.add);
        cancelBtn = (Button) findViewById(R.id.cancel);
        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.industry_spinner1);
        companySpinnerOne = (AppCompatSpinner) findViewById(R.id.company_spinner1);
        addUserData.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    private void settingListeners() {

        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        industrySpinnerOne.setOnItemSelectedListener(this);
        companySpinnerOne.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:

                if (Objects.equals(industrySpinnerOneValue, "")) {
                    industrySpinnerOne.setFocusable(true);
                    industrySpinnerOne.setFocusableInTouchMode(true);
                    industrySpinnerOne.requestFocus();
                    Utils.setSpinnerError(industrySpinnerOne, "Field can't be empty", AddUserExperience.this);
                    return;
                } else if (addUserData.getText().toString().equals("")) {
                    addUserData.requestFocus();
                    addUserData.setFocusable(true);
                    addUserData.setFocusableInTouchMode(true);
                    return;
                }
                if (!addUserData.getText().toString().equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("postNew", addUserData.getText().toString());
                    intent.putExtra("industry1", industrySpinnerOneValue);
                    intent.putExtra("company1", companySpinnerOneValue);
                    setResult(Activity.RESULT_OK, intent);

                }

                Log.e("Edit data", "" + addUserData.getText().toString());
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.industry_spinner1:
                if (position != 0) {
                    industrySpinnerOneValue = industrySpinnerOne.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest1(industrySpinnerOneValue.replaceAll("\\s+", "").substring(0, 3));
                } else {
                    industrySpinnerOneValue = "";
                }
                break;
            case R.id.company_spinner1:
                if (position != 0) {
                    companySpinnerOneValue = companySpinnerOne.getSelectedItem().toString();

                    if (companySpinnerOneValue.equals("No company to select")){
                        companySpinnerOneValue="";
                    }
                } else {
                    companySpinnerOneValue = "";
                }
                break;
        }


    }


    private MultiSpinner.MultiSpinnerListener onSelectedListener1 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(companyArrayAdapter1.getItem(i)).append(",");
                }
            }
            if (!builder.toString().equals("")) {
                companySpinnerOneValue = builder.toString();
            } else {
                companySpinnerOneValue = "";
            }
        }
    };

    private void networkCompanySpinnerArrayRequest1(String keyword) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter1.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.add(" --Select-- ");
                    companyArrayAdapter1.add("No company to select");
                    companyArrayAdapter1.notifyDataSetChanged();
                } else {
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter1.add(String.valueOf(response.get(i)));
                            companyArrayAdapter1.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(AddUserExperience.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(AddUserExperience.this);
        rQueue.add(jsonArrayRequest);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
