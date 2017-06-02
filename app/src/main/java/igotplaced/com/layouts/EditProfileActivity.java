package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import igotplaced.com.layouts.Model.Profile;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.Utils.CustomAutoCompleteView;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private String yearPassOutSpinnerValue = null;
    private ScrollView scrollView;

    private RequestQueue queue;

    private AppCompatEditText editProfileName, editProfileEmail;
    private CustomAutoCompleteView editProfileCollegeName, editProfileDepartment;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout profileName, profileEmailAddress, profileViewCollege, profileViewDepartment;
    private AppCompatButton submitbtn;
    private AppCompatCheckBox checkBoxIntrested;
    private NetworkImageView profileImage;
    private ArrayAdapter<String> departmentAutoCompleteAdapter, collegeAutoCompleteAdapter;

    private String department, college;

    private String industrySpinnerOneValue = "", industrySpinnerTwoValue = "", industrySpinnerThreeValue = "";
    private String companySpinnerOneValue = "", companySpinnerTwoValue = "", companySpinnerThreeValue = "";

    private AppCompatEditText mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne = null, industrySpinnerTwo = null, industrySpinnerThree = null;
    private MultiSpinner companySpinnerOne = null, companySpinnerTwo = null, companySpinnerThree = null;

    private ArrayAdapter<String> spinnerYearArrayAdapter, spinnerArrayAdapterI1,spinnerArrayAdapterI2,spinnerArrayAdapterI3, companyArrayAdapter1, companyArrayAdapter2, companyArrayAdapter3;
    private List<String> industrySpinnerArrayList;


    private SharedPreferences sharedpreferences;
    private String userName = null, userId = null, userEmail;

    private int yearOfPassOutSpinnerPosition = 0,industry1SpinnerPosition =0,industry2SpinnerPosition=0,industry3SpinnerPosition=0;

    private ProgressDialog pDialog;
    private String URL = BaseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addressingView();

        queue = NetworkController.getInstance(EditProfileActivity.this).getRequestQueue();

        sharedpreferences =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userName = sharedpreferences.getString(Name, null);
        userId = sharedpreferences.getString(Id, null);
        userEmail = sharedpreferences.getString(Email, null);


        addingListener();

        networkSettingSpinnerAndAutoComplete();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

        settingCheckBoxValue();
        makeJsonArrayRequestProfile();

    }

    private void settingCheckBoxValue() {
        if (checkBoxIntrested.isChecked()) {
            mobileNumberEditText.setEnabled(true);
            locationEditText.setEnabled(true);
        } else {
            mobileNumberEditText.setEnabled(false);
            locationEditText.setEnabled(false);
        }
    }

    private void addingListener() {

        editProfileName.addTextChangedListener(new CustomWatcher(editProfileName));
        editProfileEmail.addTextChangedListener(new CustomWatcher(editProfileEmail));
        editProfileCollegeName.addTextChangedListener(new CustomWatcher(editProfileCollegeName));
        editProfileDepartment.addTextChangedListener(new CustomWatcher(editProfileDepartment));
        passOutYearSpinner.setOnItemSelectedListener(this);


        checkBoxIntrested.setOnCheckedChangeListener(this);

        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));


        industrySpinnerOne.setOnItemSelectedListener(this);
        industrySpinnerTwo.setOnItemSelectedListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);/*
        companySpinnerOne.setOnItemSelectedListener(this);
        companySpinnerTwo.setOnItemSelectedListener(this);
        companySpinnerThree.setOnItemSelectedListener(this);*/

        scrollView.setOnClickListener(this);
        // checkBoxIntrested.setOnClickListener(this);

        submitbtn.setOnClickListener(this);
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


    private class CustomWatcher implements TextWatcher {

        private View view;

        CustomWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editProfileName:
                    Validation.validateName(editProfileName, profileName, EditProfileActivity.this);
                    break;
                case R.id.editProfileEmail:
                    Validation.validateEmail(editProfileEmail, profileEmailAddress, EditProfileActivity.this);
                    break;
                case R.id.profileViewCollege:
                    Validation.validateCollege(editProfileCollegeName, profileViewCollege, EditProfileActivity.this);
                    break;
                case R.id.profileViewDepartment:
                    Validation.validateDepartment(editProfileDepartment, profileViewDepartment, EditProfileActivity.this);
                    break;
                case R.id.editProfileMobileNumber:
                    if (checkBoxIntrested.isChecked())
                        Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, EditProfileActivity.this);
                    break;
                case R.id.editProfileLocation:
                    if (checkBoxIntrested.isChecked())
                        Validation.validateLocation(locationEditText, inputLayoutLocation, EditProfileActivity.this);
                    break;
            }
        }
    }


    private void networkSettingSpinnerAndAutoComplete() {


        List<String> yearList = networkYearSpinnerArrayRequest();

        spinnerYearArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);

        passOutYearSpinner.setAdapter(spinnerYearArrayAdapter);


        collegeAutoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        editProfileCollegeName.setThreshold(4);
        editProfileCollegeName.setAdapter(collegeAutoCompleteAdapter);

        editProfileCollegeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = collegeAutoCompleteAdapter.getItem(position);
            }
        });

        editProfileCollegeName.addTextChangedListener(new TextWatcher() {
            private boolean shouldAutoComplete = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
                for (int position = 0; position < collegeAutoCompleteAdapter.getCount(); position++) {
                    if (collegeAutoCompleteAdapter.getItem(position).equalsIgnoreCase(s.toString())) {
                        shouldAutoComplete = false;
                        college = null;
                        break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    if (s.toString().length() > 2) {
                        networkCollegeAutoCompleteRequest(s.toString());
                        editProfileCollegeName.showDropDown();
                    }
                }
            }


        });

        departmentAutoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        editProfileDepartment.setThreshold(3);
        editProfileDepartment.setAdapter(departmentAutoCompleteAdapter);

        editProfileDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                department = departmentAutoCompleteAdapter.getItem(position);
            }
        });

        editProfileDepartment.addTextChangedListener(new TextWatcher() {
            private boolean shouldAutoComplete = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
                for (int position = 0; position < departmentAutoCompleteAdapter.getCount(); position++) {
                    if (departmentAutoCompleteAdapter.getItem(position).equalsIgnoreCase(s.toString())) {
                        shouldAutoComplete = false;
                        department = null;
                        break;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    if (s.toString().length() > 2) {
                        networkDepartmentAutoCompleteRequest(s.toString());
                        editProfileDepartment.showDropDown();
                    }
                }

            }
        });


    }


    private List<String> networkYearSpinnerArrayRequest() {


        final List<String> yearArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/yearofpassout", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        yearArrayList.add(String.valueOf(response.get(i)));
                        spinnerYearArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//5 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);

        return yearArrayList;


    }

    private void networkCollegeAutoCompleteRequest(String keyword) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchCollege/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                collegeAutoCompleteAdapter.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        collegeAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }

    private void networkDepartmentAutoCompleteRequest(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchDepartment/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                departmentAutoCompleteAdapter.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        departmentAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);

    }

    private void settingIndustrySpinner() {
        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapterI1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);
        spinnerArrayAdapterI2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);
        spinnerArrayAdapterI3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapterI1);
        industrySpinnerTwo.setAdapter(spinnerArrayAdapterI2);
        industrySpinnerThree.setAdapter(spinnerArrayAdapterI3);


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
                        spinnerArrayAdapterI1.notifyDataSetChanged();
                        spinnerArrayAdapterI2.notifyDataSetChanged();
                        spinnerArrayAdapterI3.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private void settingCompanySpinner() {
        companyArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companySpinnerOne.setAdapter(companyArrayAdapter1, false, onSelectedListener1);
        companySpinnerTwo.setAdapter(companyArrayAdapter2, false, onSelectedListener2);
        companySpinnerThree.setAdapter(companyArrayAdapter3, false, onSelectedListener3);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {


            case R.id.profile_passed_out_year_spinner:
                if (position != 0) {
                    yearPassOutSpinnerValue = passOutYearSpinner.getSelectedItem().toString();
                } else {
                    yearPassOutSpinnerValue = null;
                }

            case R.id.profile_industry_spinner1:
                if (position != 0) {
                    industrySpinnerOneValue = industrySpinnerOne.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest1(industrySpinnerOneValue);
                } else {
                    industrySpinnerOneValue = "";
                }
                break;
         /*   case R.id.profile_company_spinner1:
                if (position != 0) {
                    companySpinnerOneValue = companySpinnerOne.getSelectedItem().toString();
                } else {
                    companySpinnerOneValue = "";
                }
                break;*/
            case R.id.profile_industry_spinner2:
                if (position != 0) {
                    industrySpinnerTwoValue = industrySpinnerTwo.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest2(industrySpinnerTwoValue);
                } else {
                    industrySpinnerTwoValue = "";
                }
                break;
            /*case R.id.profile_company_spinner2:
                if (position != 0) {
                    companySpinnerTwoValue = companySpinnerTwo.getSelectedItem().toString();
                } else {
                    companySpinnerTwoValue = "";
                }
                break;*/
            case R.id.profile_industry_spinner3:
                if (position != 0) {
                    industrySpinnerThreeValue = industrySpinnerThree.getSelectedItem().toString();
                    networkCompanySpinnerArrayRequest3(industrySpinnerThreeValue);
                } else {
                    industrySpinnerThreeValue = "";
                }
                break;
           /* case R.id.profile_company_spinner3:
                if (position != 0) {
                    companySpinnerThreeValue = companySpinnerThree.getSelectedItem().toString();
                } else {
                    companySpinnerThreeValue = "";
                }
                break;*/

        }
    }


    private void networkCompanySpinnerArrayRequest1(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter1.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.notifyDataSetChanged();
                } else {
                    companyArrayAdapter1.clear();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);


    }

    private void networkCompanySpinnerArrayRequest2(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter2.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.notifyDataSetChanged();
                } else {
                    companyArrayAdapter2.clear();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }


    private void networkCompanySpinnerArrayRequest3(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter3.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.notifyDataSetChanged();
                } else {
                    companyArrayAdapter3.clear();
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_submit:
                updateDetails();
                break;
            case R.id.checkBox:
                break;
        }
    }

    private void updateDetails() {

        if (!Validation.validateName(editProfileName, profileName, EditProfileActivity.this)) {
            return;
        }

        if (!Validation.validateEmail(editProfileEmail, profileEmailAddress, EditProfileActivity.this)) {
            return;
        }

        if (yearPassOutSpinnerValue == null) {
            passOutYearSpinner.setFocusable(true);
            passOutYearSpinner.setFocusableInTouchMode(true);
            passOutYearSpinner.requestFocus();
            Utils.setSpinnerError(passOutYearSpinner, "Field can't be empty", EditProfileActivity.this);
            return;
        }

        if (!Validation.validateCollegeCheck(editProfileCollegeName, college, profileViewCollege, EditProfileActivity.this)) {
            return;
        }

        if (!Validation.validateDepartmentCheck(editProfileDepartment, department, profileViewDepartment, EditProfileActivity.this)) {
            return;
        }
        if (Objects.equals(industrySpinnerOneValue, "")) {
            if (Objects.equals(industrySpinnerOneValue, "")) {
                industrySpinnerOne.setFocusable(true);
                industrySpinnerOne.setFocusableInTouchMode(true);
                industrySpinnerOne.requestFocus();
                Utils.setSpinnerError(industrySpinnerOne, "Field can't be empty", EditProfileActivity.this);
                return;
            }
        }
      /*  if (Objects.equals(industrySpinnerTwoValue, "") || Objects.equals(companySpinnerTwoValue, "")) {
            if (Objects.equals(industrySpinnerTwoValue, "") && !Objects.equals(companySpinnerTwoValue, "")) {
                industrySpinnerTwo.setFocusable(true);
                industrySpinnerTwo.setFocusableInTouchMode(true);
                industrySpinnerTwo.requestFocus();
                Utils.setSpinnerError(industrySpinnerTwo, "Field can't be empty", EditProfileActivity.this);
                return;
            } else if (!Objects.equals(industrySpinnerTwoValue, "") && Objects.equals(companySpinnerTwoValue, "")) {
                companySpinnerTwo.setFocusable(true);
                companySpinnerTwo.setFocusableInTouchMode(true);
                companySpinnerTwo.requestFocus();
                //Utils.setSpinnerError(companySpinnerTwo, "Field can't be empty", EditProfileActivity.this);
                return;
            }
        }

        if (Objects.equals(industrySpinnerThreeValue, "") || Objects.equals(companySpinnerThreeValue, "")) {
            if (Objects.equals(industrySpinnerThreeValue, "") && !Objects.equals(companySpinnerThreeValue, "")) {
                industrySpinnerThree.setFocusable(true);
                industrySpinnerThree.setFocusableInTouchMode(true);
                industrySpinnerThree.requestFocus();
                Utils.setSpinnerError(industrySpinnerThree, "Field can't be empty", EditProfileActivity.this);
                return;
            } else if (!Objects.equals(industrySpinnerThreeValue, "") && Objects.equals(companySpinnerThreeValue, "")) {
                companySpinnerThree.setFocusable(true);
                companySpinnerThree.setFocusableInTouchMode(true);
                companySpinnerThree.requestFocus();
               // Utils.setSpinnerError(companySpinnerThree, "Field can't be empty", EditProfileActivity.this);
                return;
            }
        }*/

        if (checkBoxIntrested.isChecked()) {

            if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, EditProfileActivity.this)) {
                return;
            }
            if (!Validation.validateLocation(locationEditText, inputLayoutLocation, EditProfileActivity.this)) {
                return;
            }
        }
        if (Utils.checkConnection(submitbtn, EditProfileActivity.this)) {
            submitDetails();
        } else {
            Utils.showDialogue(EditProfileActivity.this, "Sorry! Not connected to internet");
        }
    }

    private void submitDetails() {

        Toast.makeText(EditProfileActivity.this, "Your Profile is updated Successful", Toast.LENGTH_LONG).show();
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




    private void addressingView() {

        profileName = (TextInputLayout) findViewById(R.id.profileName);
        profileEmailAddress = (TextInputLayout) findViewById(R.id.profileEmailAddress);
        profileViewCollege = (TextInputLayout) findViewById(R.id.profileViewCollege);
        profileViewDepartment = (TextInputLayout) findViewById(R.id.profileViewDepartment);

        profileImage = (NetworkImageView) findViewById(R.id.editProfileImage);

        editProfileName = (AppCompatEditText) findViewById(R.id.editProfileName);
        editProfileEmail = (AppCompatEditText) findViewById(R.id.editProfileEmail);
        editProfileCollegeName = (CustomAutoCompleteView) findViewById(R.id.editProfileCollegeName);
        editProfileDepartment = (CustomAutoCompleteView) findViewById(R.id.editProfileDepartment);

        checkBoxIntrested = (AppCompatCheckBox) findViewById(R.id.checkBoxProfile);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);


        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editProfileMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editProfileLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner3);

        companySpinnerOne = (MultiSpinner) findViewById(R.id.profile_company_spinner1);
        companySpinnerTwo = (MultiSpinner) findViewById(R.id.profile_company_spinner2);
        companySpinnerThree = (MultiSpinner) findViewById(R.id.profile_company_spinner3);


        inputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.profileViewMobileNumberTextInputLayout);
        inputLayoutLocation = (TextInputLayout) findViewById(R.id.profileViewLocationTextInputLayout);

        submitbtn = (AppCompatButton) findViewById(R.id.profile_submit);

        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.profile_passed_out_year_spinner);

    }



    private void makeJsonArrayRequestProfile() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profileEdit/"+userId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Profile profile = new Profile(obj.getString("imgname"),obj.getString("fname"),obj.getString("department"),
                                obj.getString("college"),obj.getString("email"),obj.getString("passout"),obj.getString("interest"),
                                obj.getString("phone"),obj.getString("location"),obj.getString("industry1"),obj.getString("industry2"),
                                obj.getString("industry3"),obj.getString("company1"),obj.getString("company2"),obj.getString("company3"));


                        editProfileName.setText(profile.getProfileName());
                        editProfileEmail.setText(profile.getEmail());
                        editProfileCollegeName.setText(profile.getCollegeName());

                        checkBoxIntrested.setChecked(Boolean.parseBoolean(profile.getInterest()));

                        editProfileDepartment.setText(profile.getDepartmentName());
                        locationEditText.setText(profile.getLocation());
                        mobileNumberEditText.setText(profile.getCollegeName());

                        if (!profile.getYearOfPassOut().equals(null)) {
                            yearOfPassOutSpinnerPosition = spinnerYearArrayAdapter.getPosition(profile.getYearOfPassOut());
                            passOutYearSpinner.setSelection(yearOfPassOutSpinnerPosition);
                        }

                        if (!profile.getCompany1().equals(null)) {
                            industry1SpinnerPosition = spinnerArrayAdapterI1.getPosition(profile.getCompany1());
                            industrySpinnerOne.setSelection(industry1SpinnerPosition);
                        }

                        if (!profile.getCompany2().equals(null)) {
                            industry2SpinnerPosition = spinnerArrayAdapterI2.getPosition(profile.getCompany2());
                            industrySpinnerTwo.setSelection(industry2SpinnerPosition);
                        }

                        if (!profile.getCompany3().equals(null)) {
                            industry3SpinnerPosition = spinnerArrayAdapterI3.getPosition(profile.getCompany3());
                            industrySpinnerThree.setSelection(industry3SpinnerPosition);
                        }


                        companySpinnerOne.setAllText(profile.getCompany1());
                        companySpinnerTwo.setAllText(profile.getCompany2());
                        companySpinnerThree.setAllText(profile.getCompany3());

                        profileImage.setImageUrl(Utils.BaseImageUri + profile.getImageName(), NetworkController.getInstance(EditProfileActivity.this).getImageLoader());


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


