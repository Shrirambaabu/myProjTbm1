package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class RegisterPasswordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener, View.OnTouchListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private String industrySpinnerOneValue = "", industrySpinnerTwoValue = "", industrySpinnerThreeValue = "";
    private String companySpinnerOneValue = "", companySpinnerTwoValue = "", companySpinnerThreeValue = "";
    private ScrollView scrollView;
    private AppCompatButton regBtn;
    private AppCompatEditText passwordEditText, confirmPasswordEditText, mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout inputLayoutPassword, inputLayoutConfirmPassword, inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne = null, industrySpinnerTwo = null, industrySpinnerThree = null;
    private MultiSpinner companySpinnerOne = null, companySpinnerTwo = null, companySpinnerThree = null;
    private AppCompatCheckBox checkBoxPassword;
    private boolean checkBoxPasswordBoolean = false;

    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter1, companyArrayAdapter2, companyArrayAdapter3;
    private List<String> industrySpinnerArrayList;

    private ProgressDialog pDialog;

    private String URL = BaseUri + "/registrationService/registerPassword";

    private Intent intent;
    String userId, interest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_register_password);
        /**
         *    User defined function to
         *    map xml file to object
         **/

        initialization();

        addressingView();

        addingListener();

        settingCheckBoxValue();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Not connected to internet");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(RegisterPasswordActivity.this);
    }


    private void settingCheckBoxValue() {
        Toast.makeText(RegisterPasswordActivity.this, "" + interest, Toast.LENGTH_LONG).show();
        if (Boolean.parseBoolean(interest)) {
            checkBoxPassword.setChecked(true);
            mobileNumberEditText.setEnabled(true);
            locationEditText.setEnabled(true);
        } else {
            checkBoxPassword.setChecked(false);
            mobileNumberEditText.setEnabled(false);
            locationEditText.setEnabled(false);
        }
    }

    private void initialization() {

        intent = getIntent();

        userId = intent.getStringExtra("id");
        interest = intent.getStringExtra("interest");

    }

    private void settingIndustrySpinner() {

        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapter);
        industrySpinnerTwo.setAdapter(spinnerArrayAdapter);
        industrySpinnerThree.setAdapter(spinnerArrayAdapter);
    }

    private void settingCompanySpinner() {

        companyArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companySpinnerOne.setAdapter(companyArrayAdapter1, false, onSelectedListener1);
        companySpinnerTwo.setAdapter(companyArrayAdapter2, false, onSelectedListener2);
        companySpinnerThree.setAdapter(companyArrayAdapter3, false, onSelectedListener3);


    }

    //Passes JSON value to spinner
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
                Toast.makeText(RegisterPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(RegisterPasswordActivity.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private void networkCompanySpinnerArrayRequest1(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter1.add("");
                if(response.length()<=0){
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.add(" --Select-- ");
                    companyArrayAdapter1.add("No company to select");
                    companyArrayAdapter1.notifyDataSetChanged();
                }else {
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
                Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterPasswordActivity.this);
        rQueue.add(jsonArrayRequest);

    }


    private void networkCompanySpinnerArrayRequest2(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter2.add("");
                if(response.length()<=0){
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.add(" --Select-- ");
                    companyArrayAdapter2.add("No company to select");
                    companyArrayAdapter2.notifyDataSetChanged();
                }else {
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
                Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterPasswordActivity.this);
        rQueue.add(jsonArrayRequest);

    }

    private void networkCompanySpinnerArrayRequest3(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter3.add("");
                if(response.length()<=0){
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.add(" --Select-- ");
                    companyArrayAdapter3.add("No company to select");
                    companyArrayAdapter3.notifyDataSetChanged();
                }else {
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
                Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterPasswordActivity.this);
        rQueue.add(jsonArrayRequest);

    }


    private void addressingView() {

        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPassword);
        confirmPasswordEditText = (AppCompatEditText) findViewById(R.id.editViewConfirmPassword);
        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editViewMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.industry_spinner3);

        companySpinnerOne = (MultiSpinner) findViewById(R.id.company_spinner1);
        companySpinnerTwo = (MultiSpinner) findViewById(R.id.company_spinner2);
        companySpinnerThree = (MultiSpinner) findViewById(R.id.company_spinner3);

        checkBoxPassword = (AppCompatCheckBox) findViewById(R.id.checkBoxPassword);
        regBtn = (AppCompatButton) findViewById(R.id.register_submit);

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.ViewPasswordTextInputLayout);
        inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.ViewConfirmPasswordTextInputLayout);
        inputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.ViewMobileNumberTextInputLayout);
        inputLayoutLocation = (TextInputLayout) findViewById(R.id.ViewLocationTextInputLayout);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register_password);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

    }

    private void addingListener() {
        passwordEditText.addTextChangedListener(new CustomWatcher(passwordEditText));
        confirmPasswordEditText.addTextChangedListener(new CustomWatcher(confirmPasswordEditText));
        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));

        scrollView.setOnTouchListener(this);

        checkBoxPassword.setOnCheckedChangeListener(this);

        regBtn.setOnClickListener(this);

        industrySpinnerOne.setOnItemSelectedListener(this);
        industrySpinnerTwo.setOnItemSelectedListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);
      /*  companySpinnerOne.setOnItemSelectedListener(this);
        companySpinnerTwo.setOnItemSelectedListener(this);
        companySpinnerThree.setOnItemSelectedListener(this);*/

    }

    // Validate the user entered details
    private void finalRegistrationSuccess() {
        if (!Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validatePassword(confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validatePasswordConfirmPassword(passwordEditText, confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (Objects.equals(industrySpinnerOneValue, "")) {
            if (Objects.equals(industrySpinnerOneValue, "")) {
                industrySpinnerOne.setFocusable(true);
                industrySpinnerOne.setFocusableInTouchMode(true);
                industrySpinnerOne.requestFocus();
                Utils.setSpinnerError(industrySpinnerOne, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }
     /*   if (Objects.equals(industrySpinnerTwoValue, "") || Objects.equals(companySpinnerTwoValue, "")) {
            if (Objects.equals(industrySpinnerTwoValue, "") && !Objects.equals(companySpinnerTwoValue, "")) {
                industrySpinnerTwo.setFocusable(true);
                industrySpinnerTwo.setFocusableInTouchMode(true);
                industrySpinnerTwo.requestFocus();
                Utils.setSpinnerError(industrySpinnerTwo, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            } else if (!Objects.equals(industrySpinnerTwoValue, "") && Objects.equals(companySpinnerTwoValue, "")) {
                companySpinnerTwo.setFocusable(true);
                companySpinnerTwo.setFocusableInTouchMode(true);
                companySpinnerTwo.requestFocus();
                Utils.setSpinnerError(companySpinnerTwo, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }

        if (Objects.equals(industrySpinnerThreeValue, "") || Objects.equals(companySpinnerThreeValue, "")) {
            if (Objects.equals(industrySpinnerThreeValue, "") && !Objects.equals(companySpinnerThreeValue, "")) {
                industrySpinnerThree.setFocusable(true);
                industrySpinnerThree.setFocusableInTouchMode(true);
                industrySpinnerThree.requestFocus();
                Utils.setSpinnerError(industrySpinnerThree, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            } else if (!Objects.equals(industrySpinnerThreeValue, "") && Objects.equals(companySpinnerThreeValue, "")) {
                companySpinnerThree.setFocusable(true);
                companySpinnerThree.setFocusableInTouchMode(true);
                companySpinnerThree.requestFocus();
                Utils.setSpinnerError(companySpinnerThree, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }*/

        if (checkBoxPassword.isChecked()) {

            if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this)) {
                return;
            }
            if (!Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this)) {
                return;
            }
        }
        if (Utils.checkConnection(regBtn, RegisterPasswordActivity.this)) {
            register();
        }else{
            Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Not connected to internet");
        }


        /*Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();*/

    }

    private void register() {
        // Showing progress dialog

        pDialog = new ProgressDialog(RegisterPasswordActivity.this,R.style.MyThemeProgress);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pDialog.onBackPressed();
        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                pDialog.dismiss();

                if (Integer.parseInt(s) != 0) {
                    Toast.makeText(RegisterPasswordActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                   /* Intent registrationCompleteIntent = new Intent(RegisterPasswordActivity.this, RegisterPasswordActivity.class);
                    registrationCompleteIntent.putExtra("id",Integer.parseInt(s));
                    registrationCompleteIntent.putExtra("interest",String.valueOf(checkBoxIntrestedBoolean));
                    startActivity(registrationCompleteIntent);*/
                } else {
                    Utils.showDialogue(RegisterPasswordActivity.this, "Already Updated Your Profile!!!");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                pDialog.dismiss();
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */Log.d("error", "" + volleyError);
                Utils.showDialogue(RegisterPasswordActivity.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", userId);
                parameters.put("password", confirmPasswordEditText.getText().toString());
                parameters.put("industry1", industrySpinnerOneValue);
                parameters.put("industry2", industrySpinnerTwoValue);
                parameters.put("industry3", industrySpinnerThreeValue);
                parameters.put("company1", companySpinnerOneValue);
                parameters.put("company2", companySpinnerTwoValue);
                parameters.put("company3", companySpinnerThreeValue);
                parameters.put("phone", mobileNumberEditText.getText().toString());
                parameters.put("interest", String.valueOf((Boolean.parseBoolean(interest)) ? 1 : 0));
                parameters.put("location", locationEditText.getText().toString());
                return checkParams(parameters);
            }

            private Map<String, String> checkParams(Map<String, String> parameters) {
                Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        parameters.put(pairs.getKey(), "");
                    }
                }
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterPasswordActivity.this);
        rQueue.add(request);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.industry_spinner1:
                if (position != 0) {
                    industrySpinnerOneValue = industrySpinnerOne.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest1(industrySpinnerOneValue.replaceAll("\\s+",""));
                } else {
                    industrySpinnerOneValue = "";
                }
                break;
            /*case R.id.company_spinner1:
                if (position != 0) {
                    companySpinnerOneValue = companySpinnerOne.getSelectedItem().toString();
                } else {
                    companySpinnerOneValue = "";
                }
                break;*/
            case R.id.industry_spinner2:
                if (position != 0) {
                    industrySpinnerTwoValue = industrySpinnerTwo.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest2(industrySpinnerTwoValue.replaceAll("\\s+",""));
                } else {
                    industrySpinnerTwoValue = "";
                }
                break;
            /*case R.id.company_spinner2:
                if (position != 0) {
                    companySpinnerTwoValue = companySpinnerTwo.getSelectedItem().toString();
                } else {
                    companySpinnerTwoValue = "";
                }
                break;*/
            case R.id.industry_spinner3:
                if (position != 0) {
                    industrySpinnerThreeValue = industrySpinnerThree.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest3(industrySpinnerThreeValue.replaceAll("\\s+",""));
                } else {
                    industrySpinnerThreeValue = "";
                }
                break;
             /*case R.id.company_spinner3:
               if (position != 0) {
                    companySpinnerThreeValue = companySpinnerThree.getSelectedItem().toString();
                } else {
                    companySpinnerThreeValue = "";
                }
                break;*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.requestFocusFromTouch();
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_submit:
                finalRegistrationSuccess();
                Intent loginIntent = new Intent(new Intent(RegisterPasswordActivity.this, LoginActivity.class));
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            mobileNumberEditText.setEnabled(true);
            locationEditText.setEnabled(true);
        } else {
            mobileNumberEditText.setEnabled(false);
            locationEditText.setEnabled(false);
        }
    }

    //CustomWatcher
    private class CustomWatcher implements TextWatcher {
        private View view;

        CustomWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editTextPassword:
                    Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewConfirmPassword:
                    Validation.validatePassword(confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewMobileNumber:
                    if (checkBoxPassword.isChecked())
                        Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewLocation:
                    if (checkBoxPassword.isChecked())
                        Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this);
                    break;
            }
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



}
