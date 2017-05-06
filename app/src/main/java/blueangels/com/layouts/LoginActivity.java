package blueangels.com.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import blueangels.com.layouts.Validation.Validation;

/**
 * Created by Admin on 5/2/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText passwordEditText, emailEditText;
    private Button loginBtn;
    private String URL = "http://192.168.43.80:8080/login/rest/loginService/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_main);


        addressingView();

        addingListener();


    }


    private void addressingView() {
        emailEditText = (AppCompatEditText) findViewById(R.id.editTextEmail);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextPassword);
        loginBtn = (Button) findViewById(R.id.signInButton);
    }

    private void addingListener() {
    }

    public void forgetPassword(View view) {
        Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(forgetPasswordIntent);
    }

    public void login(View view) {
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);

        /*StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("true")){
                    Toast.makeText(LoginActivity.this, "Login Successful"+s, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email.getText().toString());
                parameters.put("password", password.getText().toString());
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
*/
    }

    public void register(View view) {
        Intent registrationIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registrationIntent);
    }


    /*public class CustomWatcher implements TextWatcher {

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
                    Validation.validateName(nameEditText, inputLayoutName, LoginActivity.this);
                    break;
                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, LoginActivity.this);
                    break;
                case R.id.editViewCollegeName:
                    Validation.validateCollege(CollegeEditText, inputLayoutCollege, LoginActivity.this);
                    break;
                case R.id.editViewDepartment:
                    Validation.validateDepartment(departmentEditText, inputLayoutDepartment, LoginActivity.this);
                    break;
            }
        }
    }*/

}
