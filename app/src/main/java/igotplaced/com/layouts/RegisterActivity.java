package igotplaced.com.layouts;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnTouchListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String yearPassOutSpinnerValue = null;
    private ScrollView scrollView;
    private AppCompatEditText nameEditText, emailEditText;
    private AppCompatAutoCompleteTextView collegeEditText, departmentEditText;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCollege, inputLayoutDepartment;
    private AppCompatButton registerBtn;
    private AppCompatCheckBox checkBoxIntrested;
    private boolean checkBoxIntrestedBoolean = false;
    private String URL = "http://192.168.43.80:8080/login/rest/loginService/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        addressingView();

        addingListener();

        String[] passOutYear = getResources().getStringArray(R.array.year_arrays);

        settingPassOutYearSpinner(passOutYear);


    }

    private void settingPassOutYearSpinner(String[] passOutYear) {

        List<String> yearList = new ArrayList<String>();

        Collections.addAll(yearList, passOutYear);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);
        passOutYearSpinner.setAdapter(spinnerArrayAdapter);

    }

    private void addressingView() {

        nameEditText = (AppCompatEditText) findViewById(R.id.editViewName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmail);
        collegeEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewCollegeName);
        departmentEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewDepartment);

        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.passed_out_year_spinner);

        inputLayoutName = (TextInputLayout) findViewById(R.id.name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.emailAddress);
        inputLayoutCollege = (TextInputLayout) findViewById(R.id.viewCollege);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.viewDepartment);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

        checkBoxIntrested = (AppCompatCheckBox) findViewById(R.id.checkBox);

        registerBtn = (AppCompatButton) findViewById(R.id.register_button);

    }


    private void addingListener() {
        nameEditText.addTextChangedListener(new CustomWatcher(nameEditText));
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));
        collegeEditText.addTextChangedListener(new CustomWatcher(collegeEditText));
        departmentEditText.addTextChangedListener(new CustomWatcher(departmentEditText));

        scrollView.setOnTouchListener(this);

        passOutYearSpinner.setOnItemSelectedListener(this);

        checkBoxIntrested.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

    }

    private void submitRegistrationDetails() {
        if (!Validation.validateName(nameEditText, inputLayoutName, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, RegisterActivity.this)) {
            return;
        }

        if (yearPassOutSpinnerValue == null) {
            passOutYearSpinner.setFocusable(true);
            passOutYearSpinner.setFocusableInTouchMode(true);
            passOutYearSpinner.requestFocus();
            Utils.setSpinnerError(passOutYearSpinner, "Field can't be empty", RegisterActivity.this);
            return;
        }

        if (!Validation.validateCollege(collegeEditText, inputLayoutCollege, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this)) {
            return;
        }

      /*  register();*/

        Intent registrationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registrationCompleteIntent);
    }

    private void register() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (s.equals("true")) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegisterActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("name", nameEditText.getText().toString());
                parameters.put("email", emailEditText.getText().toString());
                parameters.put("year", yearPassOutSpinnerValue);
                parameters.put("colg", collegeEditText.getText().toString());
                parameters.put("dept", departmentEditText.getText().toString());
                parameters.put("check", String.valueOf(checkBoxIntrestedBoolean));
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(request);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.requestFocusFromTouch();
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            yearPassOutSpinnerValue = passOutYearSpinner.getSelectedItem().toString();
        }else{
            yearPassOutSpinnerValue = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox:
                if (checkBoxIntrested.isChecked()) {
                    checkBoxIntrestedBoolean = checkBoxIntrested.isChecked();
                }
                break;
            case R.id.register_button:
                submitRegistrationDetails();
                break;
        }
    }


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
                case R.id.editViewName:
                    Validation.validateName(nameEditText, inputLayoutName, RegisterActivity.this);
                    break;
                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, RegisterActivity.this);
                    break;
                case R.id.editViewCollegeName:
                    Validation.validateCollege(collegeEditText, inputLayoutCollege, RegisterActivity.this);
                    break;
                case R.id.editViewDepartment:
                    Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this);
                    break;
            }
        }
    }

}