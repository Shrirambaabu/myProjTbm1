package blueangels.com.layouts;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blueangels.com.layouts.Validation.Validation;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity{

    private String[] passOutYear;
    private AppCompatEditText nameEditText, emailEditText;
    private AppCompatAutoCompleteTextView CollegeEditText, departmentEditText;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCollege, inputLayoutDepartment;

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
        passOutYearSpinner.setPrompt("  -- Select the Passout Year -- ");
        passOutYearSpinner.setAdapter(spinnerArrayAdapter);

    }

    private void addingListener() {
        nameEditText.addTextChangedListener(new CustomWatcher(nameEditText));
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));
        CollegeEditText.addTextChangedListener(new CustomWatcher(CollegeEditText));
        departmentEditText.addTextChangedListener(new CustomWatcher(departmentEditText));

    }

    private void addressingView() {

        nameEditText = (AppCompatEditText) findViewById(R.id.editViewName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmail);
        CollegeEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewCollegeName);
        departmentEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewDepartment);

        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.passed_out_year_spinner);

        inputLayoutName = (TextInputLayout) findViewById(R.id.name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.emailAddress);
        inputLayoutCollege = (TextInputLayout) findViewById(R.id.viewCollege);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.viewDepartment);

    }

    public void registerNew(View view) {

        /*Intent registrationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registrationCompleteIntent);*/

        submitRegistrationDetails();

    }

    private void submitRegistrationDetails() {
        if (!Validation.validateName(nameEditText, inputLayoutName, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateCollege(CollegeEditText, inputLayoutCollege, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this)) {
            return;
        }


        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
    }


    public class CustomWatcher implements TextWatcher {

        private View view;

        public CustomWatcher(View view) {
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
                    Validation.validateCollege(CollegeEditText, inputLayoutCollege, RegisterActivity.this);
                    break;
                case R.id.editViewDepartment:
                    Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this);
                    break;
            }
        }
    }

}