package igotplaced.com.layouts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class AddUserInterviewExperience extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {


    private EditText addUserData;
    private Button addBtn, cancelBtn;
    private CheckBox gotPlaced;
    private boolean checkBoxIntrestedBoolean = false;

    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter2;

    private String industrySpinnerTwoValue = "", companySpinnerTwoValue = "";
    private AppCompatSpinner industrySpinnerTwo = null;
    private AppCompatSpinner companySpinnerTwo = null;
    private List<String> industrySpinnerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_user_interview_experience);


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
        if (!isConnected){
            Utils.showDialogue(AddUserInterviewExperience.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(AddUserInterviewExperience.this);

        if (screenSize(AddUserInterviewExperience.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void settingCompanySpinner() {

        companyArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);
        companySpinnerTwo.setAdapter(companyArrayAdapter2);
    }

    private void settingIndustrySpinner() {
        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);


        industrySpinnerTwo.setAdapter(spinnerArrayAdapter);
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
                Toast.makeText(AddUserInterviewExperience.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(AddUserInterviewExperience.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private void mapping() {
        addUserData = (EditText) findViewById(R.id.getUserInterviewData);
        addUserData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.getUserInterviewData) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                return false;
            }
        });
        addBtn = (Button) findViewById(R.id.add);
        cancelBtn = (Button) findViewById(R.id.cancel);
        gotPlaced = (CheckBox) findViewById(R.id.checkBoxPlaced);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.industry_spinner2);
        companySpinnerTwo = (AppCompatSpinner) findViewById(R.id.company_spinner2);
    }

    private void settingListeners() {

        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        gotPlaced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (gotPlaced.isChecked()) {
                    checkBoxIntrestedBoolean = true;
                }else {
                    checkBoxIntrestedBoolean = false;

                }
            }
        });
        industrySpinnerTwo.setOnItemSelectedListener(this);
        companySpinnerTwo.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add:

                if (Objects.equals(industrySpinnerTwoValue, "")) {
                    industrySpinnerTwo.setFocusable(true);
                    industrySpinnerTwo.setFocusableInTouchMode(true);
                    industrySpinnerTwo.requestFocus();
                    Utils.setSpinnerError(industrySpinnerTwo, "Field can't be empty", AddUserInterviewExperience.this);
                    return;
                }
                else if (addUserData.getText().toString().equals("")) {
                    addUserData.requestFocus();
                    addUserData.setFocusable(true);
                    addUserData.setFocusableInTouchMode(true);
                    return;
                }
                if (!addUserData.getText().toString().equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("interviewNew", addUserData.getText().toString());
                    intent.putExtra("interest", String.valueOf(checkBoxIntrestedBoolean));
                    intent.putExtra("industry2", industrySpinnerTwoValue);
                    intent.putExtra("company2", companySpinnerTwoValue);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.industry_spinner2:
                if (position != 0) {
                    industrySpinnerTwoValue = industrySpinnerTwo.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest2(industrySpinnerTwoValue.replaceAll("\\s+", "").substring(0, 3));
                } else {
                    industrySpinnerTwoValue = "";
                }
                break;
            case R.id.company_spinner2:
                if (position != 0) {
                    companySpinnerTwoValue = companySpinnerTwo.getSelectedItem().toString();

                    if (companySpinnerTwoValue.equals("No company to select")){
                        companySpinnerTwoValue="";
                    }
                } else {
                    companySpinnerTwoValue = "";
                }
                break;
        }
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener2 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(companyArrayAdapter2.getItem(i)).append(",");
                }
            }
            if (!builder.toString().equals("")) {
                companySpinnerTwoValue = builder.toString();
            } else {
                companySpinnerTwoValue = "";
            }
        }
    };

    private void networkCompanySpinnerArrayRequest2(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter2.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.add(" --Select-- ");
                    companyArrayAdapter2.add("No company to select");
                    companyArrayAdapter2.notifyDataSetChanged();
                } else {
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter2.add(String.valueOf(response.get(i)));
                            companyArrayAdapter2.notifyDataSetChanged();
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
                Utils.showDialogue(AddUserInterviewExperience.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(AddUserInterviewExperience.this);
        rQueue.add(jsonArrayRequest);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
