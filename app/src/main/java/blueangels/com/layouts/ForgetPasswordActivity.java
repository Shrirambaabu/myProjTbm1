package blueangels.com.layouts;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import blueangels.com.layouts.Validation.Validation;

/**
 * Created by Ashith VL on 5/2/2017.
 */

public class ForgetPasswordActivity extends AppCompatActivity {


    private AppCompatEditText emailEditText;
    private TextInputLayout inputLayoutEmail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_forget_password);

        addressingView();

        addingListener();
        
    }

    private void addingListener() {
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));
    }

    private void addressingView() {

        emailEditText = (AppCompatEditText) findViewById(R.id.editTextEmail);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

    }

    public void forgetPassword(View view) {

        submittingForgetPassword();

    }

    private void submittingForgetPassword() {
        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, ForgetPasswordActivity.this)) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Email send Successfully", Toast.LENGTH_SHORT).show();
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
                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, ForgetPasswordActivity.this);
                    break;
            }
        }
    }

}
