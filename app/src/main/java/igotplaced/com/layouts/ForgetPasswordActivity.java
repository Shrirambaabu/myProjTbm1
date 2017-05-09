package igotplaced.com.layouts;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import igotplaced.com.layouts.Utils.Validation;

/**
 * Created by Ashith VL on 5/2/2017.
 */

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private AppCompatEditText emailEditText;
    private TextInputLayout inputLayoutEmail;
    private Button forgetPasswordSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_forget_password);

        addressingView();

        addingListener();

    }

    private void addingListener() {
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));
        forgetPasswordSubmit.setOnClickListener(this);
    }

    private void addressingView() {

        emailEditText = (AppCompatEditText) findViewById(R.id.editTextEmail);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        forgetPasswordSubmit = (Button) findViewById(R.id.forget_password_button);

    }

    private void submittingForgetPassword() {
        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, ForgetPasswordActivity.this)) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Email send Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password_button:
                submittingForgetPassword();
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
                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, ForgetPasswordActivity.this);
                    break;
            }
        }
    }

}
