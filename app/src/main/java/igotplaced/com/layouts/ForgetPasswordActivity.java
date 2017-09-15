package igotplaced.com.layouts;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
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
import static igotplaced.com.layouts.Utils.Utils.screenSize;

/**
 * Created by Ashith VL on 5/2/2017.
 */

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener  {


    private AppCompatEditText emailEditText;
    private TextInputLayout inputLayoutEmail;
    private Button forgetPasswordSubmit;

    private String URL = BaseUri + "/ForgetPasswordService/forgotPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_forget_password);
        /**
         *    User defined function to
         *    map xml file to object
         **/
        addressingView();
        // Adding click Listener
        addingListener();

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            Utils.showDialogue(ForgetPasswordActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(ForgetPasswordActivity.this);

        if (screenSize(ForgetPasswordActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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

    // performs action when submit is clicked
    private void submittingForgetPassword() {
        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, ForgetPasswordActivity.this)) {
            return;
        }

        if (Utils.checkConnection(forgetPasswordSubmit, ForgetPasswordActivity.this)) {
            forgetPassword();
        }else{
            Utils.showDialogue(ForgetPasswordActivity.this, "Sorry! Not connected to internet");
        }

    }

    private void forgetPassword() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (Integer.parseInt(s) != 0) {

                    Toast.makeText(getApplicationContext(), "Email send Successfully", Toast.LENGTH_SHORT).show();

                   finish();
                } else {
                    Utils.showDialogue(ForgetPasswordActivity.this, "Sorry!!! Wrong email id");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(ForgetPasswordActivity.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", emailEditText.getText().toString());
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);
        rQueue.add(request);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password_button:
                submittingForgetPassword();//forget password submit method
                break;
        }
    }

    //customWatcher
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
