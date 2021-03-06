package igotplaced.com.layouts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import igotplaced.com.layouts.Model.Profile;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.Utils.ConnectivityReceiver;
import igotplaced.com.layouts.Utils.CustomAutoCompleteView;
import igotplaced.com.layouts.Utils.MultiSpinnerMark;
import igotplaced.com.layouts.Utils.MyApplication;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;
import igotplaced.com.layouts.Utils.Validation;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Email;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private String yearPassOutSpinnerValue = null;
    private ScrollView scrollView;

    private RequestQueue queue;

    private AppCompatEditText editProfileName, editProfileEmail;
    private CustomAutoCompleteView editProfileCollegeName, editProfileDepartment;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout profileName, profileEmailAddress, profileViewCollege, profileViewDepartment;
    private AppCompatButton submitbtn;
    private AppCompatCheckBox checkBoxIntrested;

    private ArrayAdapter<String> departmentAutoCompleteAdapter, collegeAutoCompleteAdapter;
    private List<String> itemsCompanySpinnerOne, itemsCompanySpinnerTwo, itemsCompanySpinnerThree;
    private String department, college;

    private String industrySpinnerOneValue = "", industrySpinnerTwoValue = "", industrySpinnerThreeValue = "";
    private String companySpinnerOneValue = "", companySpinnerTwoValue = "", companySpinnerThreeValue = "";
    private AppCompatEditText mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne = null, industrySpinnerTwo = null, industrySpinnerThree = null;
    private MultiSpinner companySpinnerOne = null, companySpinnerTwo = null, companySpinnerThree = null;
    private ArrayAdapter<String> spinnerYearArrayAdapter, spinnerArrayAdapterI1, spinnerArrayAdapterI2, spinnerArrayAdapterI3, companyArrayAdapter1, companyArrayAdapter2, companyArrayAdapter3;
    private List<String> industrySpinnerArrayList;
    private boolean checkBoxIntrestedBoolean = false;
    private String userId = null;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addressingView();

        queue = NetworkController.getInstance(EditProfileActivity.this).getRequestQueue();

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userId = sharedpreferences.getString(Id, null);

        itemsCompanySpinnerOne = new ArrayList<String>();
        itemsCompanySpinnerTwo = new ArrayList<String>();
        itemsCompanySpinnerThree = new ArrayList<String>();
        addingListener();


        pDialog = new ProgressDialog(EditProfileActivity.this, R.style.MyThemeProgress);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pDialog.onBackPressed();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        networkSettingSpinnerAndAutoComplete();

        //Setting industry spinner value
        settingIndustrySpinner();

        //Setting company spinner value
        settingCompanySpinner();

        settingCheckBoxValue();

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                makeJsonArrayRequestProfile();
            }
        };

        handler.postDelayed(runnable, 3000);

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Utils.showDialogue(EditProfileActivity.this, "Sorry! Not connected to internet");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(EditProfileActivity.this);

        if (screenSize(EditProfileActivity.this) > 8.5) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void settingCheckBoxValue() {
        if (checkBoxIntrested.isChecked()) {
            mobileNumberEditText.setEnabled(true);
            locationEditText.setEnabled(true);
        } else {
            mobileNumberEditText.setEnabled(false);
            locationEditText.setEnabled(false);
        }
    }

    private void addingListener() {

        editProfileName.addTextChangedListener(new CustomWatcher(editProfileName));
        editProfileEmail.addTextChangedListener(new CustomWatcher(editProfileEmail));
        editProfileCollegeName.addTextChangedListener(new CustomWatcher(editProfileCollegeName));
        editProfileDepartment.addTextChangedListener(new CustomWatcher(editProfileDepartment));
        passOutYearSpinner.setOnItemSelectedListener(this);


        checkBoxIntrested.setOnCheckedChangeListener(this);

        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));


        industrySpinnerOne.setOnItemSelectedListener(this);
        industrySpinnerTwo.setOnItemSelectedListener(this);
        industrySpinnerThree.setOnItemSelectedListener(this);

        scrollView.setOnClickListener(this);


        submitbtn.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mobileNumberEditText.setEnabled(true);
            locationEditText.setEnabled(true);
            checkBoxIntrestedBoolean = true;
        } else {
            mobileNumberEditText.setEnabled(false);
            locationEditText.setEnabled(false);
            mobileNumberEditText.setText("");
            locationEditText.setText("");
            checkBoxIntrestedBoolean = false;
        }
    }


    private class CustomWatcher implements TextWatcher {

        private View view;

        CustomWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editProfileName:
                    Validation.validateName(editProfileName, profileName, EditProfileActivity.this);
                    break;
                case R.id.editProfileEmail:
                    Validation.validateEmail(editProfileEmail, profileEmailAddress, EditProfileActivity.this);
                    break;
                case R.id.profileViewCollege:
                    Validation.validateCollege(editProfileCollegeName, profileViewCollege, EditProfileActivity.this);
                    break;
                case R.id.profileViewDepartment:
                    Validation.validateDepartment(editProfileDepartment, profileViewDepartment, EditProfileActivity.this);
                    break;
                case R.id.editProfileMobileNumber:
                    if (checkBoxIntrested.isChecked())
                        Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, EditProfileActivity.this);
                    break;
                case R.id.editProfileLocation:
                    if (checkBoxIntrested.isChecked())
                        Validation.validateLocation(locationEditText, inputLayoutLocation, EditProfileActivity.this);
                    break;
            }
        }
    }


    private void networkSettingSpinnerAndAutoComplete() {


        List<String> yearList = networkYearSpinnerArrayRequest();

        spinnerYearArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);

        passOutYearSpinner.setAdapter(spinnerYearArrayAdapter);


        collegeAutoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        editProfileCollegeName.setThreshold(4);
        editProfileCollegeName.setAdapter(collegeAutoCompleteAdapter);

        editProfileCollegeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                college = collegeAutoCompleteAdapter.getItem(position);
            }
        });

        editProfileCollegeName.addTextChangedListener(new TextWatcher() {
            private boolean shouldAutoComplete = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
                for (int position = 0; position < collegeAutoCompleteAdapter.getCount(); position++) {
                    if (collegeAutoCompleteAdapter.getItem(position).equalsIgnoreCase(s.toString())) {
                        shouldAutoComplete = false;
                        college = null;
                        break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    if (s.toString().length() > 2) {
                        networkCollegeAutoCompleteRequest(s.toString());
                        editProfileCollegeName.showDropDown();
                    }
                }
            }


        });

        departmentAutoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        editProfileDepartment.setThreshold(3);
        editProfileDepartment.setAdapter(departmentAutoCompleteAdapter);

        editProfileDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                department = departmentAutoCompleteAdapter.getItem(position);
            }
        });

        editProfileDepartment.addTextChangedListener(new TextWatcher() {
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
                        department = null;
                        break;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    if (s.toString().length() > 2) {
                        networkDepartmentAutoCompleteRequest(s.toString());
                        editProfileDepartment.showDropDown();
                    }
                }

            }
        });


    }


    private List<String> networkYearSpinnerArrayRequest() {


        final List<String> yearArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/yearofpassout", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        yearArrayList.add(String.valueOf(response.get(i)));
                        spinnerYearArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//5 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);

        return yearArrayList;


    }

    private void networkCollegeAutoCompleteRequest(String keyword) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchCollege/" + keyword.replaceAll("\\s", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                collegeAutoCompleteAdapter.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        collegeAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }

    private void networkDepartmentAutoCompleteRequest(String keyword) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/autocompleteService/searchDepartment/" + keyword.replaceAll("\\s", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                departmentAutoCompleteAdapter.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        departmentAutoCompleteAdapter.add(String.valueOf(response.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 3000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);

    }

    private void settingIndustrySpinner() {
        List<String> industryList = networkIndustrySpinnerArrayRequest();
        spinnerArrayAdapterI1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);
        spinnerArrayAdapterI2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);
        spinnerArrayAdapterI3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, industryList);

        industrySpinnerOne.setAdapter(spinnerArrayAdapterI1);
        industrySpinnerTwo.setAdapter(spinnerArrayAdapterI2);
        industrySpinnerThree.setAdapter(spinnerArrayAdapterI3);


    }

    private List<String> networkIndustrySpinnerArrayRequest() {


        industrySpinnerArrayList = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/industry", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                industrySpinnerArrayList.add("-- Select --");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        industrySpinnerArrayList.add(String.valueOf(response.get(i)));
                        spinnerArrayAdapterI1.notifyDataSetChanged();
                        spinnerArrayAdapterI2.notifyDataSetChanged();
                        spinnerArrayAdapterI3.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        // Adding request to request queue
        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
        //displays the selected spinner value
        return industrySpinnerArrayList;
    }

    private void settingCompanySpinner() {
        companyArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companyArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item_custom);

        companySpinnerOne.setAdapter(companyArrayAdapter1, false, onSelectedListener1);
        companySpinnerTwo.setAdapter(companyArrayAdapter2, false, onSelectedListener2);
        companySpinnerThree.setAdapter(companyArrayAdapter3, false, onSelectedListener3);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {


            case R.id.profile_passed_out_year_spinner:
                if (position != 0) {
                    yearPassOutSpinnerValue = passOutYearSpinner.getSelectedItem().toString();
                } else {
                    yearPassOutSpinnerValue = null;
                }

            case R.id.profile_industry_spinner1:

                if (position != 0) {
                    industrySpinnerOneValue = industrySpinnerOne.getSelectedItem().toString();

                    networkCompanySpinnerArrayRequest1(industrySpinnerOneValue.replaceAll("\\s+", "").substring(0, 2));


                } else {
                    industrySpinnerOneValue = "";
                }

                break;

            case R.id.profile_industry_spinner2:

                if (position != 0) {
                    industrySpinnerTwoValue = industrySpinnerTwo.getSelectedItem().toString();

                    networkCompanySpinnerArrayRequest2(industrySpinnerTwoValue.replaceAll("\\s+", "").substring(0, 2));
                } else {
                    industrySpinnerTwoValue = "";
                }

                break;

            case R.id.profile_industry_spinner3:

                if (position != 0) {
                    industrySpinnerThreeValue = industrySpinnerThree.getSelectedItem().toString();

                    networkCompanySpinnerArrayRequest3(industrySpinnerThreeValue.replaceAll("\\s+", "").substring(0, 2));
                } else {
                    industrySpinnerThreeValue = "";
                }

                break;

        }
    }


    private void networkCompanySpinnerArrayRequest1(String keyword) {


        companySpinnerOne.setDefaultText(" --Select-- ");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter1.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.add(" --Select-- ");
                    companyArrayAdapter1.add("No company to select");
                    companyArrayAdapter1.notifyDataSetChanged();
                } else {
                    companyArrayAdapter1.clear();
                    companyArrayAdapter1.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter1.add(String.valueOf(response.get(i)));
                            companyArrayAdapter1.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                if (!itemsCompanySpinnerOne.isEmpty()) {
                    // set initial selection
                    boolean[] selectedItems = new boolean[companyArrayAdapter1.getCount()];
                    List<String> companyItems = new ArrayList<>();
                    for (int iq = 0; iq < companyArrayAdapter1.getCount(); iq++) {
                        companyItems.add(companyArrayAdapter1.getItem(iq));

                    }

                    for (int l = 0; l < itemsCompanySpinnerOne.size(); l++) {

                        if (companyItems.indexOf(itemsCompanySpinnerOne.get(l)) != -1) {
                            selectedItems[companyItems.indexOf(itemsCompanySpinnerOne.get(l))] = true;

                        }
                    }
                    companySpinnerOne.setSelected(selectedItems);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);


    }

    private void networkCompanySpinnerArrayRequest2(String keyword) {

        companySpinnerTwo.setDefaultText(" --Select-- ");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter2.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.add(" --Select-- ");
                    companyArrayAdapter2.add("No company to select");
                    companyArrayAdapter2.notifyDataSetChanged();
                } else {
                    companyArrayAdapter2.clear();
                    companyArrayAdapter2.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter2.add(String.valueOf(response.get(i)));
                            companyArrayAdapter2.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                if (!itemsCompanySpinnerTwo.isEmpty()) {
                    boolean[] selectedItems = new boolean[companyArrayAdapter2.getCount()];


                    List<String> companyItems = new ArrayList<>();
                    for (int iq = 0; iq < companyArrayAdapter2.getCount(); iq++) {
                        companyItems.add(companyArrayAdapter2.getItem(iq));

                    }

                    for (int l = 0; l < itemsCompanySpinnerTwo.size(); l++) {

                        if (companyItems.indexOf(itemsCompanySpinnerTwo.get(l)) != -1) {
                            selectedItems[companyItems.indexOf(itemsCompanySpinnerTwo.get(l))] = true;

                        }
                    }
                    companySpinnerTwo.setSelected(selectedItems);
                } else {
                    companySpinnerTwo.setDefaultText(" --Select-- ");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }


    private void networkCompanySpinnerArrayRequest3(String keyword) {

        companySpinnerThree.setDefaultText(" --Select-- ");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/spinner/company/" + keyword, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                companyArrayAdapter3.add("");
                if (response.length() <= 0) {
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.add(" --Select-- ");
                    companyArrayAdapter3.add("No company to select");
                    companyArrayAdapter3.notifyDataSetChanged();
                } else {
                    companyArrayAdapter3.clear();
                    companyArrayAdapter3.add(" --Select-- ");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            companyArrayAdapter3.add(String.valueOf(response.get(i)));
                            companyArrayAdapter3.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!itemsCompanySpinnerThree.isEmpty()) {

                    boolean[] selectedItems = new boolean[companyArrayAdapter3.getCount()];
                    List<String> companyItems = new ArrayList<>();
                    for (int iq = 0; iq < companyArrayAdapter3.getCount(); iq++) {
                        companyItems.add(companyArrayAdapter3.getItem(iq));

                    }

                    for (int l = 0; l < itemsCompanySpinnerThree.size(); l++) {

                        if (companyItems.indexOf(itemsCompanySpinnerThree.get(l)) != -1) {
                            selectedItems[companyItems.indexOf(itemsCompanySpinnerThree.get(l))] = true;

                        }
                    }
                    companySpinnerThree.setSelected(selectedItems);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /**
                 *  Returns error message when,
                 *  server is down,
                 *  incorrect IP
                 *  Server not deployed
                 */
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        });


        int MY_SOCKET_TIMEOUT_MS = 5000;//3 seconds - change to what you want
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(jsonArrayRequest);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_submit:
                updateDetails();
                break;

        }
    }


    private void updateDetails() {

        if (!Validation.validateName(editProfileName, profileName, EditProfileActivity.this)) {
            return;
        }

        if (!Validation.validateEmail(editProfileEmail, profileEmailAddress, EditProfileActivity.this)) {
            return;
        }

        if (yearPassOutSpinnerValue == null) {
            passOutYearSpinner.setFocusable(true);
            passOutYearSpinner.setFocusableInTouchMode(true);
            passOutYearSpinner.requestFocus();
            Utils.setSpinnerError(passOutYearSpinner, "Field can't be empty", EditProfileActivity.this);
            return;
        }

        if (!Validation.validateCollegeCheck(editProfileCollegeName, college, profileViewCollege, EditProfileActivity.this)) {
            return;
        }

        if (!Validation.validateDepartmentCheck(editProfileDepartment, department, profileViewDepartment, EditProfileActivity.this)) {
            return;
        }
        if (Objects.equals(industrySpinnerOneValue, "")) {
            if (Objects.equals(industrySpinnerOneValue, "")) {
                industrySpinnerOne.setFocusable(true);
                industrySpinnerOne.setFocusableInTouchMode(true);
                industrySpinnerOne.requestFocus();
                Utils.setSpinnerError(industrySpinnerOne, "Field can't be empty", EditProfileActivity.this);
                return;
            }
        }


        if (checkBoxIntrested.isChecked()) {

            if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, EditProfileActivity.this)) {
                return;
            }
            if (!Validation.validateLocation(locationEditText, inputLayoutLocation, EditProfileActivity.this)) {
                return;
            }
        }
        if (Utils.checkConnection(submitbtn, EditProfileActivity.this)) {
            submitDetails();


        } else {
            Utils.showDialogue(EditProfileActivity.this, "Sorry! Not connected to internet");
        }
    }

    private void submitDetails() {// Showing progress dialog

        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, BaseUri + "/profileService/profileUpdate/" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                pDialog.dismiss();

                if (Integer.parseInt(s) != 0) {
                    Toast.makeText(EditProfileActivity.this, "Your profile updated Successfully", Toast.LENGTH_LONG).show();
                    Intent profileUpdated = new Intent(new Intent(EditProfileActivity.this, MainActivity.class));
                    profileUpdated.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(profileUpdated);
                } else {
                    Utils.showDialogue(EditProfileActivity.this, "Already Your Profile is upp-to date!!!");
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
                Utils.showDialogue(EditProfileActivity.this, "Sorry! Server Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", userId);
                parameters.put("name", editProfileName.getText().toString());
                parameters.put("email", editProfileEmail.getText().toString());
                parameters.put("year", yearPassOutSpinnerValue);
                parameters.put("colg", editProfileCollegeName.getText().toString());
                parameters.put("dept", editProfileDepartment.getText().toString());
                parameters.put("industry1", industrySpinnerOneValue);
                parameters.put("industry2", industrySpinnerTwoValue);
                parameters.put("industry3", industrySpinnerThreeValue);
                parameters.put("company1", companySpinnerOneValue);
                parameters.put("company2", companySpinnerTwoValue);
                parameters.put("company3", companySpinnerThreeValue);
                parameters.put("phone", mobileNumberEditText.getText().toString());
                parameters.put("interest", String.valueOf((checkBoxIntrestedBoolean) ? 1 : 0));
                parameters.put("location", locationEditText.getText().toString());

                Log.e("Update", "" + parameters);

                return checkParams(parameters);
            }

            private Map<String, String> checkParams(Map<String, String> parameters) {
                Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        parameters.put(pairs.getKey(), "");
                    }
                }
                return parameters;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 30000;//30 seconds - change to what you want
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(request);

    }


    private MultiSpinner.MultiSpinnerListener onSelectedListener1 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[0] = false;
                    companySpinnerOne.setSelected(selected);
                    builder.append(companyArrayAdapter1.getItem(i)).append(",");
                }
            }
            if (!builder.toString().equals("")&&!builder.toString().equals(" --Select-- ,")&&!builder.toString().equals("No company to select,")) {
                companySpinnerOneValue = builder.toString().replaceAll(",$", "").replaceAll(" --Select-- ,", "");

            } else {
                companySpinnerOneValue = "";

            }
        }
    };


    private MultiSpinner.MultiSpinnerListener onSelectedListener2 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[0] = false;
                    companySpinnerTwo.setSelected(selected);
                    builder.append(companyArrayAdapter2.getItem(i)).append(",");
                }
            }
            if (!builder.toString().equals("")&&!builder.toString().equals(" --Select-- ,")&&!builder.toString().equals("No company to select,")) {
                companySpinnerTwoValue = builder.toString().replaceAll(",$", "").replaceAll(" --Select-- ,", "");
            } else {
                companySpinnerTwoValue = "";
            }
        }
    };


    private MultiSpinner.MultiSpinnerListener onSelectedListener3 = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            companySpinnerThree.setVisibility(View.VISIBLE);
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    selected[0] = false;
                    companySpinnerThree.setSelected(selected);
                    builder.append(companyArrayAdapter3.getItem(i)).append(",");
                }
            }

            if (!builder.toString().equals("")&&!builder.toString().equals(" --Select-- ,")&&!builder.toString().equals("No company to select,")) {
                companySpinnerThreeValue = builder.toString().replaceAll(",$", "").replaceAll(" --Select-- ,", "");
            } else {
                companySpinnerThreeValue = "";
            }
        }
    };


    private void addressingView() {

        profileName = (TextInputLayout) findViewById(R.id.profileName);
        profileEmailAddress = (TextInputLayout) findViewById(R.id.profileEmailAddress);
        profileViewCollege = (TextInputLayout) findViewById(R.id.profileViewCollege);
        profileViewDepartment = (TextInputLayout) findViewById(R.id.profileViewDepartment);


        editProfileName = (AppCompatEditText) findViewById(R.id.editProfileName);
        editProfileEmail = (AppCompatEditText) findViewById(R.id.editProfileEmail);
        editProfileCollegeName = (CustomAutoCompleteView) findViewById(R.id.editProfileCollegeName);
        editProfileDepartment = (CustomAutoCompleteView) findViewById(R.id.editProfileDepartment);

        checkBoxIntrested = (AppCompatCheckBox) findViewById(R.id.checkBoxProfile);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);


        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editProfileMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editProfileLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.profile_industry_spinner3);

        companySpinnerOne = (MultiSpinner) findViewById(R.id.profile_company_spinner1);
        companySpinnerTwo = (MultiSpinner) findViewById(R.id.profile_company_spinner2);
        companySpinnerThree = (MultiSpinner) findViewById(R.id.profile_company_spinner3);
        companySpinnerOne.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_ATOP);
        companySpinnerTwo.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_ATOP);
        companySpinnerThree.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_ATOP);

        inputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.profileViewMobileNumberTextInputLayout);
        inputLayoutLocation = (TextInputLayout) findViewById(R.id.profileViewLocationTextInputLayout);

        submitbtn = (AppCompatButton) findViewById(R.id.profile_submit);

        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.profile_passed_out_year_spinner);

    }

    private void makeJsonArrayRequestProfile() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profileEdit/" + userId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                pDialog.dismiss();

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Profile profile = new Profile(obj.getString("imgname"), obj.getString("fname"), obj.getString("department"),
                                obj.getString("college"), obj.getString("email"), obj.getString("passout"), obj.getString("interest"),
                                obj.getString("phone"), obj.getString("location"), obj.getString("industry1"), obj.getString("industry2"),
                                obj.getString("industry3"), obj.getString("company1"), obj.getString("company2"), obj.getString("company3"));


                        editProfileName.setText(profile.getProfileName());
                        editProfileEmail.setText(profile.getEmail());
                        //  editProfileCollegeName.setText(profile.getCollegeName());
                        editProfileCollegeName.setText(profile.getCollegeName());
                        college = profile.getCollegeName();
                        editProfileDepartment.setText(profile.getDepartmentName());
                        department = profile.getDepartmentName();
                        if (profile.getInterest().equals("1")) {
                            checkBoxIntrested.setChecked(true);
                        } else {
                            checkBoxIntrested.setChecked(false);
                        }

                        // editProfileDepartment.setText(profile.getDepartmentName());
                        locationEditText.setText(profile.getLocation());
                        mobileNumberEditText.setText(profile.getMobileNumber());

                        if (!profile.getYearOfPassOut().equals(null)) {
                            int yearOfPassOutSpinnerPosition = 0;
                            yearOfPassOutSpinnerPosition = spinnerYearArrayAdapter.getPosition(profile.getYearOfPassOut());
                            passOutYearSpinner.setSelection(yearOfPassOutSpinnerPosition);
                        }

                        if (!profile.getIndustry1().equals(null)) {
                            int industry1SpinnerPosition = 0;
                            industry1SpinnerPosition = spinnerArrayAdapterI1.getPosition(profile.getIndustry1());
                            industrySpinnerOne.setSelection(industry1SpinnerPosition);
                            industrySpinnerOneValue = profile.getIndustry1();
                        }

                        if (!profile.getIndustry2().equals(null)) {
                            int industry2SpinnerPosition = 0;
                            industry2SpinnerPosition = spinnerArrayAdapterI2.getPosition(profile.getIndustry2());
                            industrySpinnerTwo.setSelection(industry2SpinnerPosition);
                            industrySpinnerTwoValue = profile.getIndustry2();
                        }

                        if (!profile.getIndustry3().equals(null)) {
                            int industry3SpinnerPosition = 0;
                            industry3SpinnerPosition = spinnerArrayAdapterI3.getPosition(profile.getIndustry3());
                            industrySpinnerThree.setSelection(industry3SpinnerPosition);
                            industrySpinnerThreeValue = profile.getIndustry3();
                        }


                        if (profile.getCompany1().isEmpty()) {
                            companySpinnerOne.setText("");
                            companySpinnerOneValue = "";

                        } else {

                            itemsCompanySpinnerOne = Arrays.asList(profile.getCompany1().split("\\s*,\\s*"));

                            companySpinnerOne.setText(profile.getCompany1());
                            companySpinnerOneValue = profile.getCompany1();

                        }

                        if (profile.getCompany2().isEmpty()) {
                            companySpinnerTwo.setText("");
                            companySpinnerTwoValue = "";

                        } else {
                            itemsCompanySpinnerTwo = Arrays.asList(profile.getCompany2().split("\\s*,\\s*"));


                            companySpinnerTwo.setText(profile.getCompany2());
                            companySpinnerTwoValue = profile.getCompany2();
                        }
                        if (profile.getCompany3().isEmpty()) {
                            companySpinnerThree.setText("");
                            companySpinnerThreeValue = "";

                        } else {
                            itemsCompanySpinnerThree = Arrays.asList(profile.getCompany3().split("\\s*,\\s*"));


                            companySpinnerThree.setText(profile.getCompany3());
                            companySpinnerThreeValue = profile.getCompany3();
                        }

                        NetworkImageView profileImage = (NetworkImageView) findViewById(R.id.editProfileImage);

                        profileImage.setImageUrl(Utils.BaseImageUri + profile.getImageName(), NetworkController.getInstance(EditProfileActivity.this).getImageLoader());


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();

            }
        });

        queue.add(jsonArrayRequest);

    }


}


