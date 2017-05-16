package igotplaced.com.layouts;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import igotplaced.com.layouts.Utils.CustomAutoCompleteView;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, AdapterView.OnItemSelectedListener {


    private String yearPassOutSpinnerValue = null;
    private ScrollView scrollView;
    private AppCompatEditText nameEditText, emailEditText;
    private CustomAutoCompleteView collegeEditText, departmentEditText;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCollege, inputLayoutDepartment;
    private AppCompatButton registerBtn;
    private AppCompatCheckBox checkBoxIntrested;
    private boolean checkBoxIntrestedBoolean = false;
    private ArrayAdapter<String> spinnerArrayAdapter,departmentAutoCompleteAdapter;

    private ProgressDialog pDialog;

    private String URL = BaseUri + "/registrationService/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);


        addressingView();

        addingListener();

        networkSettingSpinnerAndAutoComplete();

    }

    private void networkSettingSpinnerAndAutoComplete() {

        List<String> yearList = networkYearSpinnerArrayRequest();

        spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);

        passOutYearSpinner.setAdapter(spinnerArrayAdapter);
/*


        List<String> collegeList = networkCollegeAutoCompleteRequest();

        String[] college = collegeList.toArray(new String[collegeList.size()]);
*/

        String[] college = {"C", "C++", "Java", ".NET", "iPhone", "Android", "ASP.NET", "PHP"};

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> CollegeAutoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, college);

        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView AutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.editViewCollegeName);
        AutoCompleteTextView.setThreshold(3);
        AutoCompleteTextView.setAdapter(CollegeAutoCompleteAdapter);



/*
        List<String> DepartmentList = networkDepartmentAutoCompleteRequest();

        String[] department = DepartmentList.toArray(new String[DepartmentList.size()]);

        Toast.makeText(RegisterActivity.this, "" + DepartmentList.toString(), Toast.LENGTH_LONG).show();*/


        //Creating the instance of ArrayAdapter containing list of language names
        departmentAutoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

        departmentEditText.setThreshold(3);
        departmentEditText.setAdapter(departmentAutoCompleteAdapter);

        departmentEditText.addTextChangedListener(new TextWatcher() {
            private boolean shouldAutoComplete = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
                for (int position = 0; position < departmentAutoCompleteAdapter.getCount(); position++) {
                    if (departmentAutoCompleteAdapter.getItem(position).equalsIgnoreCase(s.toString())) {
                        shouldAutoComplete = false;
                        break;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    if(s.toString().length()>2){
                        networkDepartmentAutoCompleteRequest(s.toString());
                        departmentEditText.showDropDown();
                    }
                }

            }
        });


    }


    private void networkDepartmentAutoCompleteRequest(String keyword) {
/*

        final List<String> DepartmentList = new ArrayList<String>();

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("LoadingAU...");
        pDialog.show();
*/

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchDepartment/"+keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              /*  pDialog.dismiss();
*/
                Log.d("error", response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        departmentAutoCompleteAdapter.clear();
                        departmentAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
/*
                pDialog.dismiss();*/
                Log.d("error", "here22" + error.toString());
            }
        });
/*

        Toast.makeText(RegisterActivity.this, "" + DepartmentList.toString(), Toast.LENGTH_LONG).show();*/

        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(jsonArrayRequest);

/*
        return DepartmentList;*/
    }

    private List<String> networkCollegeAutoCompleteRequest() {

        final List<String> ColgList = new ArrayList<String>();

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("LoadingAU...");
        pDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchCollege", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();

                Log.d("error", response.toString());

                Toast.makeText(RegisterActivity.this, "" + response.toString(), Toast.LENGTH_LONG).show();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        ColgList.add(String.valueOf(response.get(i)));
                        spinnerArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                Log.d("error", "here22" + error.toString());
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(jsonArrayRequest);

        return ColgList;
    }


    private List<String> networkYearSpinnerArrayRequest() {

        final List<String> yearArrayList = new ArrayList<String>();

       /* pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/yearofpassout", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               /* pDialog.dismiss();*/

                Log.d("error", response.toString());


                for (int i = 0; i < response.length(); i++) {
                    try {
                        yearArrayList.add(String.valueOf(response.get(i)));
                        spinnerArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             /*   pDialog.dismiss();*/

                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.d("error", "here" + error.toString());
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(jsonArrayRequest);

        return yearArrayList;
    }

    private void addressingView() {

        nameEditText = (AppCompatEditText) findViewById(R.id.editViewName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmail);
        collegeEditText = (CustomAutoCompleteView) findViewById(R.id.editViewCollegeName);
        departmentEditText = (CustomAutoCompleteView) findViewById(R.id.editViewDepartment);

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

/*
        passOutYearSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());*/

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

        register();

        /*Intent registrationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registrationCompleteIntent);*/
    }

    private void register() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(RegisterActivity.this, "returned -> " + s, Toast.LENGTH_LONG).show();


/*
                if (s.equals("true")) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
                }*/

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
                parameters.put("check", String.valueOf((checkBoxIntrestedBoolean) ? 1 : 0));
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
        } else {
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

 /*   private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(parent.getContext(), "On Item Select : \n" + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }*/
}