package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import igotplaced.com.layouts.Utils.MyService;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

/**
 * Created by Admin on 5/2/2017.
 */

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private ProgressDialog pDialog;
    private AppCompatEditText passwordEditText, emailEditText;
    private Button loginBtn;
    private TextView register;
    private TextInputLayout inputLayoutEmail;
    private String URL = BaseUri + "/loginService/login"; // Login URL


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_login_main);

        /**
         *    User defined function to
         *    map xml file to object
         **/
        addressingView();

        // Adding click Listener
        addingListener();

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Log.e("Conn", "Change");
            Utils.showDialogue(LoginActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(LoginActivity.this);
        if (screenSize(LoginActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void addressingView() {
        emailEditText = (AppCompatEditText) findViewById(R.id.editTextEmail);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextPassword);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.viewEmail);
        loginBtn = (Button) findViewById(R.id.signInButton);
        register = (TextView) findViewById(R.id.button);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    private void addingListener() {
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));

        loginBtn.setOnClickListener(this);
        register.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:

                submitLoginDetails();
                break;
            case R.id.button:
                register();
        }

    }


    /**
     * Redirects to new activity when
     * forget password is clicked
     */
    public void forgetPassword(View view) {
        if (Utils.checkConnection(loginBtn, LoginActivity.this)) {
            Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(forgetPasswordIntent);
        } else {
            Utils.showDialogue(LoginActivity.this, "Sorry! Not connected to internet");
        }
    }


    private void submitLoginDetails() {

        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, LoginActivity.this)) {
            return;
        }
        //Broadcast receiver to check internet connection
        if (Utils.checkConnection(loginBtn, LoginActivity.this)) {
            //Performs action when Login button is clicked
            login();
        } else {
            Utils.showDialogue(LoginActivity.this, "Sorry! Not connected to internet");
        }
    }

    @Override
    public void onBackPressed() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        new AlertDialog.Builder(ctw).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LoginActivity.super.onBackPressed();

                    }
                }).setNegativeButton("No", null).show();


    }

    private void login() {

        // Showing progress dialog

        pDialog = new ProgressDialog(LoginActivity.this, R.style.MyThemeProgress);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pDialog.onBackPressed();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        //Requests the data from webservice using volley
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();
                // Verify the data and return the Toast message
                if (!s.equals("false")) {

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    String[] tokens = s.split(",", -1);

                    editor.putString(Id, tokens[0]);
                    editor.putString(Name, tokens[1]);
                    editor.putString(Email, emailEditText.getText().toString().trim());
                    editor.commit();

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);

                } else {
                    Utils.showDialogue(LoginActivity.this, "Sorry!!! Incorrect Password");
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
                 */
                Utils.showDialogue(LoginActivity.this, "Sorry! Server Error");
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


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);

    }

    public void register() {
        if (Utils.checkConnection(loginBtn, LoginActivity.this)) {
            Intent registrationIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registrationIntent);
        } else {
            Utils.showDialogue(LoginActivity.this, "Sorry! Not connected to internet");
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

                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, LoginActivity.this);
                    break;

            }
        }
    }

}
