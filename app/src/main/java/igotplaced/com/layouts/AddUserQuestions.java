package igotplaced.com.layouts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.Window;
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

public class AddUserQuestions extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {


    private EditText addUserData;
    private Button addBtn, cancelBtn;
    private String industrySpinnerThreeValue = "", companySpinnerThreeValue = "";
    private AppCompatSpinner industrySpinnerThree = null;
    private AppCompatSpinner companySpinnerThree = null;
    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter3;
    private List<String> industrySpinnerArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_user_questions);

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
            Utils.showDialogue(AddUserQuestions.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(AddUserQuestions.this);

        if (screenSize(AddUserQuestions.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void settingCompanySpinner() {

        companyArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);
        companySpinnerThree.setAdapter(companyArrayAdapter3);
    }

    private void settingIndustrySpinner() {
        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerThree.setAdapter(spinnerArrayAdapter);
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
                Toast.makeText(AddUserQuestions.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(AddUserQuestions.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener3 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(companyArrayAdapter3.getItem(i)).append(",");
                }
            }

            if (!builder.toString().equals("")) {
                companySpinnerThreeValue = builder.toString();
            } else {
                companySpinnerThreeValue = "";
            }
        }
    };

    private void mapping() {
        addUserData = (EditText) findViewById(R.id.getUserQuestionsData);
        addBtn = (Button) findViewById(R.id.add);
        cancelBtn = (Button) findViewById(R.id.cancel);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.industry_spinner3);
        companySpinnerThree = (AppCompatSpinner) findViewById(R.id.company_spinner3);
    }

    private void settingListeners() {

        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);
        companySpinnerThree.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:

                    if (Objects.equals(industrySpinnerThreeValue, "")) {
                        industrySpinnerThree.setFocusable(true);
                        industrySpinnerThree.setFocusableInTouchMode(true);
                        industrySpinnerThree.requestFocus();
                        Utils.setSpinnerError(industrySpinnerThree, "Field can't be empty", AddUserQuestions.this);
                        return;
                    }

                if (!addUserData.getText().toString().equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("questionsNew", addUserData.getText().toString());
                    intent.putExtra("industry3", industrySpinnerThreeValue);
                    intent.putExtra("company3", companySpinnerThreeValue);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if (addUserData.getText().toString().equals("")) {
                    addUserData.requestFocus();
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
            case R.id.industry_spinner3:
                if (position != 0) {
                    industrySpinnerThreeValue = industrySpinnerThree.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest3(industrySpinnerThreeValue.replaceAll("\\s+", "").substring(0, 2));
                } else {
                    industrySpinnerThreeValue = "";
                }
                break;
            case R.id.company_spinner3:
                if (position != 0) {
                    companySpinnerThreeValue = companySpinnerThree.getSelectedItem().toString();

                } else {
                    companySpinnerThreeValue = "";
                }
                break;
        }
    }

    private void networkCompanySpinnerArrayRequest3(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter3.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.add(" --Select-- ");
                    companyArrayAdapter3.add("No company to select");
                    companyArrayAdapter3.notifyDataSetChanged();
                } else {
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter3.add(String.valueOf(response.get(i)));
                            companyArrayAdapter3.notifyDataSetChanged();
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
                Utils.showDialogue(AddUserQuestions.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(AddUserQuestions.this);
        rQueue.add(jsonArrayRequest);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
