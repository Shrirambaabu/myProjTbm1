package igotplaced.com.layouts;

import android.app.ProgressDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class RegisterPasswordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {


    private String industrySpinnerOneValue = null, industrySpinnerTwoValue = null, industrySpinnerThreeValue = null;
    private String companySpinnerOneValue = null, companySpinnerTwoValue = null, companySpinnerThreeValue = null;
    private ScrollView scrollView;
    private AppCompatButton regBtn;
    private AppCompatEditText passwordEditText, confirmPasswordEditText, mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout inputLayoutPassword, inputLayoutConfirmPassword, inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne, industrySpinnerTwo, industrySpinnerThree, companySpinnerOne, companySpinnerTwo, companySpinnerThree;
    private AppCompatCheckBox checkBoxPassword;
    private boolean checkBoxPasswordBoolean = false;

    private ArrayAdapter<String> spinnerArrayAdapter, companyArrayAdapter;
    private List<String> industrySpinnerArrayList;

    private ProgressDialog pDialog;
    private  Bundle extras;
    String userid, interest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       extras = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_register_password);
        /**
         *    User defined function to
         *    map xml file to object
         **/
        addressingView();

        addingListener();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

        userid = extras.getString("id");
        interest = extras.getString("interest");


    }


    private void settingIndustrySpinner() {

        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapter);
        industrySpinnerTwo.setAdapter(spinnerArrayAdapter);
        industrySpinnerThree.setAdapter(spinnerArrayAdapter);
    }

    private void settingCompanySpinner() {

        List<String> companyList = networkCompanySpinnerArrayRequest();
        companyArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, companyList);

        companySpinnerOne.setAdapter(companyArrayAdapter);
        companySpinnerTwo.setAdapter(companyArrayAdapter);
        companySpinnerThree.setAdapter(companyArrayAdapter);

    }

    //Pases JSON value to spinner
    private List<String> networkIndustrySpinnerArrayRequest() {

        industrySpinnerArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/industry", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

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

    private List<String> networkCompanySpinnerArrayRequest() {

        final List<String> companySpinnerOneArrayList = new ArrayList<String>();

        return companySpinnerOneArrayList;
    }


    private void addressingView() {

        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPassword);
        confirmPasswordEditText = (AppCompatEditText) findViewById(R.id.editViewConfirmPassword);
        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editViewMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.industry_spinner3);

        companySpinnerOne = (AppCompatSpinner) findViewById(R.id.company_spinner1);
        companySpinnerTwo = (AppCompatSpinner) findViewById(R.id.company_spinner2);
        companySpinnerThree = (AppCompatSpinner) findViewById(R.id.company_spinner3);

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

        checkBoxPassword.setOnClickListener(this);
        regBtn.setOnClickListener(this);

        industrySpinnerOne.setOnItemSelectedListener(this);
        industrySpinnerTwo.setOnItemSelectedListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);
        companySpinnerOne.setOnItemSelectedListener(this);
        companySpinnerTwo.setOnItemSelectedListener(this);
        companySpinnerThree.setOnItemSelectedListener(this);

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
        if (industrySpinnerOneValue == null || companySpinnerOneValue == null) {
            if (industrySpinnerOneValue == null) {
                industrySpinnerOne.setFocusable(true);
                industrySpinnerOne.setFocusableInTouchMode(true);
                industrySpinnerOne.requestFocus();
                Utils.setSpinnerError(industrySpinnerOne, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            } else {
                companySpinnerOne.setFocusable(true);
                companySpinnerOne.setFocusableInTouchMode(true);
                companySpinnerOne.requestFocus();
                Utils.setSpinnerError(companySpinnerOne, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }
        if (industrySpinnerTwoValue == null || companySpinnerTwoValue == null) {
            if (industrySpinnerTwoValue == null && companySpinnerTwoValue != null) {
                industrySpinnerTwo.setFocusable(true);
                industrySpinnerTwo.setFocusableInTouchMode(true);
                industrySpinnerTwo.requestFocus();
                Utils.setSpinnerError(industrySpinnerTwo, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            } else if (industrySpinnerTwoValue != null && companySpinnerTwoValue == null) {
                companySpinnerTwo.setFocusable(true);
                companySpinnerTwo.setFocusableInTouchMode(true);
                companySpinnerTwo.requestFocus();
                Utils.setSpinnerError(companySpinnerTwo, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }

        if (industrySpinnerThreeValue == null || companySpinnerThreeValue == null) {
            if (industrySpinnerThreeValue == null && companySpinnerThreeValue != null) {
                industrySpinnerThree.setFocusable(true);
                industrySpinnerThree.setFocusableInTouchMode(true);
                industrySpinnerThree.requestFocus();
                Utils.setSpinnerError(industrySpinnerThree, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            } else if (industrySpinnerThreeValue != null && companySpinnerThreeValue == null) {
                companySpinnerThree.setFocusable(true);
                companySpinnerThree.setFocusableInTouchMode(true);
                companySpinnerThree.requestFocus();
                Utils.setSpinnerError(companySpinnerThree, "Field can't be empty", RegisterPasswordActivity.this);
                return;
            }
        }

        if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this)) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.industry_spinner1:
                if (position != 0) {
                    industrySpinnerOneValue = industrySpinnerOne.getSelectedItem().toString();
                } else {
                    industrySpinnerOneValue = null;
                }
                break;
            case R.id.company_spinner1:
                if (position != 0) {
                    companySpinnerOneValue = companySpinnerOne.getSelectedItem().toString();
                } else {
                    companySpinnerOneValue = null;
                }
                break;
            case R.id.industry_spinner2:
                if (position != 0) {
                    industrySpinnerTwoValue = industrySpinnerTwo.getSelectedItem().toString();
                } else {
                    industrySpinnerTwoValue = null;
                }
                break;
            case R.id.company_spinner2:
                if (position != 0) {
                    companySpinnerTwoValue = companySpinnerTwo.getSelectedItem().toString();
                } else {
                    companySpinnerTwoValue = null;
                }
                break;
            case R.id.industry_spinner3:
                if (position != 0) {
                    industrySpinnerThreeValue = industrySpinnerThree.getSelectedItem().toString();
                } else {
                    industrySpinnerThreeValue = null;
                }
                break;
            case R.id.company_spinner3:
                if (position != 0) {
                    companySpinnerThreeValue = companySpinnerThree.getSelectedItem().toString();
                } else {
                    companySpinnerThreeValue = null;
                }
                break;
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
                break;
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
                    Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewLocation:
                    Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this);
                    break;
            }
        }
    }
}
