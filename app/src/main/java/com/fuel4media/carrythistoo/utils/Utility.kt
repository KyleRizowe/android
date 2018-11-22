package com.fuel4media.carrythistoo.utils


import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.provider.Settings
import android.provider.Settings.System.getString
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.fuel4media.carrythistoo.CarryThisTooApplication
import com.fuel4media.carrythistoo.R
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by shweta on 21/12/17.
 */

object Utility {

    fun getDeviceId(): String {
        return if (CarryThisTooApplication.applicationContext() == null) "" else getString(CarryThisTooApplication.applicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    fun fullScreenSize(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = activity.window
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun showProgress(context: Context, message: String): ProgressDialog {
        val progressDialog: ProgressDialog
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
        return progressDialog
    }


    fun isValidString(string: String): Boolean {
        return !TextUtils.isEmpty(string)
    }


    /* Validation of Email */
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }


    fun showProgressBarSmall(rlProgressBar: RelativeLayout) {
        if (rlProgressBar != null && rlProgressBar.visibility == View.GONE) {
            rlProgressBar.visibility = View.VISIBLE
            setProgressBarColor(rlProgressBar.findViewById(R.id.progress_bar))
        }
    }

    private fun setProgressBarColor(progressBar: ProgressBar) {
        if (Build.VERSION.SDK_INT <= 21) {
            progressBar.indeterminateDrawable
                    .setColorFilter(ContextCompat.getColor(CarryThisTooApplication.applicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        }
    }

    fun hideProgressBar(rlProgressBar: RelativeLayout?) {
        if (rlProgressBar != null && rlProgressBar.visibility == View.VISIBLE) {
            rlProgressBar.visibility = View.GONE
        }
    }


    fun isValidPassword(password: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }


    fun isValidMobileNumber(mobileNumber: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^[+][0-9]{8,20}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(mobileNumber)

        return matcher.matches()

    }

    fun convertBooleanToInt(value: Boolean): Int {
        return if (value) 1 else 0
    }

    fun convertIntToBoolean(value: Int): Boolean {
        return value == 1
    }

    fun showKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

    }

    fun hideKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
