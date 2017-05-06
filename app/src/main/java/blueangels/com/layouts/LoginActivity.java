package blueangels.com.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

/**
 * Created by Admin on 5/2/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText email, password;
    private Button logIn;
    private String URL = "http://192.168.43.80:8080/login/rest/loginService/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_main);

        email = (AppCompatEditText) findViewById(R.id.editTextEmail);
        password = (AppCompatEditText) findViewById(R.id.editTextPassword);
       logIn = (Button) findViewById(R.id.signInButton);

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

}
