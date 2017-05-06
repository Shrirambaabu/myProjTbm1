package blueangels.com.layouts;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import blueangels.com.layouts.Validation.Validation;

public class RegisterPasswordActivity extends AppCompatActivity {


    private AppCompatEditText passwordEditText, confirmPasswordEditText, mobileNumberEditText, locationEditText;
    private TextInputLayout inputLayoutPassword, inputLayoutconfirmPassword, inputLayoutmobileNumber, inputLayoutlocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_register_password);

        addressingView();

        addingListener();
    }

    private void addingListener() {
        passwordEditText.addTextChangedListener(new CustomWatcher(passwordEditText));
        confirmPasswordEditText.addTextChangedListener(new CustomWatcher(confirmPasswordEditText));
        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));
    }

    private void addressingView() {
        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPassword);
        confirmPasswordEditText = (AppCompatEditText) findViewById(R.id.editViewConfirmPassword);
        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editViewMobileNumber);
        locationEditText = (AppCompatEditText) findViewById(R.id.editViewLocation);

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.editViewPasswordTextInputLayout);
        inputLayoutconfirmPassword = (TextInputLayout) findViewById(R.id.editViewConfirmPasswordTextInputLayout);
        inputLayoutmobileNumber = (TextInputLayout) findViewById(R.id.editViewMobileNumberTextInputLayout);
        inputLayoutlocation = (TextInputLayout) findViewById(R.id.editViewLocationTextInputLayout);


    }

    public void RegistrationSuccess(View view) {

        finalRegistrationSuccess();

    }

    private void finalRegistrationSuccess() {
        if (!Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if(!Validation.validatePassword(confirmPasswordEditText, inputLayoutconfirmPassword, RegisterPasswordActivity.this)){
            return;
        }
        if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutmobileNumber, RegisterPasswordActivity.this)){
            return;
        }
        if (!Validation.validateLocation(locationEditText, inputLayoutlocation, RegisterPasswordActivity.this)){
            return;
        }
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
                case R.id.editTextPassword:
                    Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewConfirmPassword:
                    Validation.validatePassword(confirmPasswordEditText, inputLayoutconfirmPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewMobileNumber:
                    Validation.validateMobileNumber(mobileNumberEditText, inputLayoutmobileNumber, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewLocation:
                    Validation.validateLocation(locationEditText, inputLayoutlocation, RegisterPasswordActivity.this);
                    break;
            }
        }
    }
}
