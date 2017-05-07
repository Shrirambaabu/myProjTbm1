package blueangels.com.layouts.Utils;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import blueangels.com.layouts.R;

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
            inputLayoutDepartment.setError(activity.getString(R.string.err_msg_department));
            requestFocus(departmentEditText, activity);
            return false;
        } else {
            inputLayoutDepartment.setErrorEnabled(false);
        }

        return true;
    }


    public static boolean validatePassword(AppCompatEditText passwordEditText, TextInputLayout inputLayoutPassword, Activity activity) {
        if (passwordEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().length() < 4) {
            inputLayoutPassword.setError(activity.getString(R.string.err_msg_password));
            requestFocus(passwordEditText, activity);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }


    public static boolean validateMobileNumber(AppCompatEditText mobileNumberEditText, TextInputLayout inputLayoutMobileNumber, Activity activity) {

        if (mobileNumberEditText.getText().toString().trim().isEmpty() || mobileNumberEditText.getText().length() != 10) {
            inputLayoutMobileNumber.setError(activity.getString(R.string.err_msg_mobile));
            requestFocus(mobileNumberEditText, activity);
            return false;
        } else {
            inputLayoutMobileNumber.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean validateLocation(AppCompatAutoCompleteTextView locationEditText, TextInputLayout inputLayoutLocation, Activity activity) {
        if (locationEditText.getText().toString().trim().isEmpty() || locationEditText.getText().length() < 2) {
            inputLayoutLocation.setError(activity.getString(R.string.err_msg_location));
            requestFocus(locationEditText, activity);
            return false;
        } else {
            inputLayoutLocation.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean validatePasswordConfirmPassword(AppCompatEditText passwordEditText, AppCompatEditText confirmPasswordEditText, TextInputLayout inputLayoutConfirmPassword, Activity activity) {

        if (!passwordEditText.getText().toString().trim().equals(confirmPasswordEditText.getText().toString().trim())) {
            inputLayoutConfirmPassword.setError(activity.getString(R.string.err_msg_confirm_password));
            requestFocus(inputLayoutConfirmPassword, activity);
            return false;
        } else {
            inputLayoutConfirmPassword.setEnabled(false);
        }
        return true;
    }

    public static void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}
