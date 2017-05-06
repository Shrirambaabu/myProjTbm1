package blueangels.com.layouts.Validation;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import blueangels.com.layouts.R;
import blueangels.com.layouts.RegisterActivity;

/**
 * Created by Ashith VL on 5/6/2017.
 */

public class Validation {

    public static boolean validateName(AppCompatEditText nameEditText, TextInputLayout inputLayoutName, Activity activity) {
        if (nameEditText.getText().toString().trim().isEmpty() || nameEditText.getText().length() < 3) {
            inputLayoutName.setError(activity.getString(R.string.err_msg_name));
            requestFocus(nameEditText, activity);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateEmail(AppCompatEditText emailEditText, TextInputLayout inputLayoutEmail, Activity activity) {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(activity.getString(R.string.err_msg_email));
            requestFocus(emailEditText, activity);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validateCollege(AppCompatAutoCompleteTextView collegeEditText, TextInputLayout inputLayoutCollege, Activity activity) {
        if (collegeEditText.getText().toString().trim().isEmpty() || collegeEditText.getText().length() < 3) {
            inputLayoutCollege.setError(activity.getString(R.string.err_msg_college));
            requestFocus(collegeEditText, activity);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateDepartment(AppCompatAutoCompleteTextView departmentEditText, TextInputLayout inputLayoutDepartment, Activity activity) {
        if (departmentEditText.getText().toString().trim().isEmpty() || departmentEditText.getText().length() < 3) {
            inputLayoutDepartment.setError(activity.getString(R.string.err_msg_departmrnt));
            requestFocus(departmentEditText, activity);
            return false;
        } else {
            inputLayoutDepartment.setErrorEnabled(false);
        }

        return true;
    }


    public static boolean validatePassword(AppCompatEditText passwordEditText, TextInputLayout inputLayoutpassword, Activity activity) {
        if (passwordEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().length() < 4) {
            inputLayoutpassword.setError(activity.getString(R.string.err_msg_password));
            requestFocus(passwordEditText, activity);
            return false;
        } else {
            inputLayoutpassword.setErrorEnabled(false);
        }
        return true;
    }


    public static boolean validateMobileNumber(AppCompatEditText mobileNumberEditText, TextInputLayout inputLayoutmobileNumber, Activity activity) {

        if (mobileNumberEditText.getText().toString().trim().isEmpty() || mobileNumberEditText.getText().length() < 10) {
            requestFocus(mobileNumberEditText, activity);
            return false;
        } else {
            inputLayoutmobileNumber.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean validateLocation(AppCompatEditText locationEditText, TextInputLayout inputLayoutlocation, Activity activity) {
        if (locationEditText.getText().toString().trim().isEmpty() || locationEditText.getText().length() < 2) {
            requestFocus(locationEditText, activity);
            return false;
        } else {
            inputLayoutlocation.setErrorEnabled(false);
        }
        return true;
    }

    public static void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
