package igotplaced.com.layouts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

/**
 * Created by Admin on 5/2/2017.
 */

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private AppCompatEditText passwordEditText, emailEditText;
    private Button loginBtn;
    private TextInputLayout inputLayoutEmail;
    private String URL = BaseUri+"/loginService/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_login_main);

        addressingView();

        addingListener();

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Utils.showDialogue(LoginActivity.this,"Sorry! Not connected to internet");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(LoginActivity.this);
    }

    private void addressingView() {
        emailEditText = (AppCompatEditText) findViewById(R.id.editTextEmail);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextPassword);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.viewEmail);
        loginBtn = (Button) findViewById(R.id.signInButton);
    }

    private void addingListener() {
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitLoginDetails();

        /*Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);*/

            }
        });

    }

    public void forgetPassword(View view) {
        Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(forgetPasswordIntent);
    }

    private void submitLoginDetails() {
        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, LoginActivity.this)) {
            return;
        }

        Utils.checkConnection(loginBtn,LoginActivity.this);

        login();

    }

    private void login() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (s.equals("true")) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Utils.showDialogue(LoginActivity.this,"Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", emailEditText.getText().toString().trim());
                parameters.put("password", passwordEditText.getText().toString().trim());
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 3000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);

    }

    public void register(View view) {
        Intent registrationIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registrationIntent);
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
                    Validation.validateEmail(emailEditText, inputLayoutEmail, LoginActivity.this);
                    break;

            }
        }
    }

}
