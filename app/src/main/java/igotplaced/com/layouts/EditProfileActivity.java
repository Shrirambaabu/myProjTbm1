package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Utils.CustomAutoCompleteView;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String yearPassOutSpinnerValue = null;
    private ScrollView scrollView;
    private AppCompatEditText editProfileName, editProfileEmail;
    private CustomAutoCompleteView editProfileCollegeName, editProfileDepartment;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout profileName, profileEmailAddress, profileViewCollege, profileViewDepartment;
    private AppCompatButton submitbtn;
    private AppCompatCheckBox checkBoxIntrested;
    private boolean checkBoxIntrestedBoolean = false;
    private ArrayAdapter<String> departmentAutoCompleteAdapter, collegeAutoCompleteAdapter;

    private String department,college;


    private String industrySpinnerOneValue = "", industrySpinnerTwoValue = "", industrySpinnerThreeValue = "";
    private String companySpinnerOneValue = "", companySpinnerTwoValue = "", companySpinnerThreeValue = "";

    private AppCompatButton regBtn;
    private AppCompatEditText  mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout  inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne = null, industrySpinnerTwo = null, industrySpinnerThree = null, companySpinnerOne = null, companySpinnerTwo = null, companySpinnerThree = null;
    private AppCompatCheckBox checkBoxPassword;

    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter1, companyArrayAdapter2, companyArrayAdapter3;
    private List<String> industrySpinnerArrayList;


    private Intent intent;
    String userId, interest;


    private ProgressDialog pDialog;
    private String URL = BaseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addressingView();

        addingListener();

        networkSettingSpinnerAndAutoComplete();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

    }

    private void settingCompanySpinner() {


        companyArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companySpinnerOne.setAdapter(companyArrayAdapter1);
        companySpinnerTwo.setAdapter(companyArrayAdapter2);
        companySpinnerThree.setAdapter(companyArrayAdapter3);

    }

    private void settingIndustrySpinner() {


        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapter);
        industrySpinnerTwo.setAdapter(spinnerArrayAdapter);
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
                Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;

    }


    private void networkSettingSpinnerAndAutoComplete() {



        List<String> yearList = networkYearSpinnerArrayRequest();

        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);

        passOutYearSpinner.setAdapter(spinnerArrayAdapter);


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
                    if(s.toString().length()>2){
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
                    if(s.toString().length()>2){
                        networkDepartmentAutoCompleteRequest(s.toString());
                        editProfileDepartment.showDropDown();
                    }
                }

            }
        });


    }

    private void networkDepartmentAutoCompleteRequest(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchDepartment/"+keyword, new Response.Listener<JSONArray>() {
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

    private void networkCollegeAutoCompleteRequest(String keyword) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchCollege/"+keyword, new Response.Listener<JSONArray>() {
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

    private List<String> networkYearSpinnerArrayRequest() {

        final List<String> yearArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/yearofpassout", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        yearArrayList.add(String.valueOf(response.get(i)));
                        spinnerArrayAdapter.notifyDataSetChanged();
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

    private void addingListener() {

        editProfileName.addTextChangedListener(new CustomWatcher(editProfileName));
        editProfileEmail.addTextChangedListener(new CustomWatcher(editProfileEmail));
        editProfileCollegeName.addTextChangedListener(new CustomWatcher(editProfileCollegeName));
        editProfileDepartment.addTextChangedListener(new CustomWatcher(editProfileDepartment));
        passOutYearSpinner.setOnItemSelectedListener(this);



        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));


        industrySpinnerOne.setOnItemSelectedListener(this);
        industrySpinnerTwo.setOnItemSelectedListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);
        companySpinnerOne.setOnItemSelectedListener(this);
        companySpinnerTwo.setOnItemSelectedListener(this);
        companySpinnerThree.setOnItemSelectedListener(this);



        scrollView.setOnTouchListener((View.OnTouchListener) EditProfileActivity.this);
        checkBoxIntrested.setOnClickListener((View.OnClickListener) this);

        submitbtn.setOnClickListener((View.OnClickListener) this);
    }

    private void addressingView() {

        profileName =(TextInputLayout) findViewById(R.id.profileName);
        profileEmailAddress=(TextInputLayout) findViewById(R.id.profileEmailAddress);
        profileViewCollege = (TextInputLayout) findViewById(R.id.profileViewCollege);
        profileViewDepartment = (TextInputLayout) findViewById(R.id.profileViewDepartment);

        checkBoxIntrested = (AppCompatCheckBox) findViewById(R.id.checkBox);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);


        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editProfileMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editProfileLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner3);

        companySpinnerOne = (AppCompatSpinner) findViewById(R.id.profile_company_spinner1);
        companySpinnerTwo = (AppCompatSpinner) findViewById(R.id.profile_company_spinner2);
        companySpinnerThree = (AppCompatSpinner) findViewById(R.id.profile_company_spinner3);



        inputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.profileViewMobileNumberTextInputLayout);
        inputLayoutLocation = (TextInputLayout) findViewById(R.id.profileViewLocationTextInputLayout);

        submitbtn=(AppCompatButton) findViewById(R.id.profile_submit) ;


        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.profile_passed_out_year_spinner);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            yearPassOutSpinnerValue = passOutYearSpinner.getSelectedItem().toString();
        } else {
            yearPassOutSpinnerValue = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class CustomWatcher implements TextWatcher{

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
                case R.id.editViewMobileNumber:
                    if (checkBoxPassword.isChecked())
                        Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, EditProfileActivity.this);
                    break;
                case R.id.editViewLocation:
                    if (checkBoxPassword.isChecked())
                        Validation.validateLocation(locationEditText, inputLayoutLocation, EditProfileActivity.this);
                    break;
            }
        }
    }
}
