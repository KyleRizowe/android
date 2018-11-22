package com.fuel4media.carrythistoo.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by Admin on 6/29/2017.
 */

class CommonMethods {
    internal var listDialog: Dialog? = null

    companion object {

        internal var dialog: ProgressDialog? = null

        fun isEmpty(view: View): Boolean {
            var isEmpty = true
            if (view is EditText) {
                isEmpty = TextUtils.isEmpty(view.text.toString().trim { it <= ' ' })
            } else if (view is TextView) {
                isEmpty = TextUtils.isEmpty(view.text.toString().trim { it <= ' ' })
            }

            return isEmpty
        }

        fun showLongToast(context: Context, message: String) {

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()

        }

        fun showShortToast(context: Context, message: String) {

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()

        }


        fun isEmailValid(Email: String): Boolean {
            var isValid = false

            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(Email)
            if (matcher.matches()) {
                isValid = true
            }
            return isValid
        }

        fun validateEmail(inputStr: String): Boolean {

            var isValid = false


            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(inputStr)
            if (matcher.matches()) {
                isValid = true
            }

            return isValid
        }

        fun GetDialog(context: Context, title: String, Message: String): ProgressDialog {


            if (dialog != null) {
                dialog = null
            }
            dialog = ProgressDialog(context)
            dialog!!.setTitle(title)
            dialog!!.setMessage(Message)
            dialog!!.show()
            return dialog as ProgressDialog
        }


        fun dismissDialog() {
            dialog!!.dismiss()
        }

        fun errormessageon_Edittext(message: String, view: EditText) {

            val ecolor = Color.parseColor("#ff0000") // whatever color you want
            val fgcspan = ForegroundColorSpan(ecolor)
            val ssbuilder = SpannableStringBuilder(message)
            ssbuilder.setSpan(fgcspan, 0, message.length, 0)
            view.error = ssbuilder
            view.requestFocus()
        }

        /**
         * method used to get current time
         *
         * @return date and time both
         */
        //        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        val formattedCurrentTime: String
            get() {
                val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val date = Date()
                return formatter.format(date)
            }

        fun getLinearLayoutParams(width: Int, height: Int): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(width, height)
        }

        /**
         * method used to show alert dialog
         *
         * @param string get alert message
         */
        fun showAlert(context: Context, title: String, string: String) {
            // TODO Auto-generated method stub
            val alert = AlertDialog.Builder(context)
            alert.setTitle(title)
            alert.setMessage(string)
            alert.setPositiveButton("Ok") { dialog, which ->
                // TODO Auto-generated method stub
            }
            alert.show()

        }

        /**
         * method used to validate email
         *
         * @param email get email
         * @return true or false
         */
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        /**
         * method used to get age from date of birth
         *
         * @param _month month
         * @param _day   day
         * @param _year  year
         * @return age
         */
        fun getAge(_month: Int, _day: Int, _year: Int): Int {

            val cal = GregorianCalendar()
            val year: Int
            val month: Int
            val date: Int
            var age: Int

            year = cal.get(Calendar.YEAR)
            month = cal.get(Calendar.MONTH)
            date = cal.get(Calendar.DAY_OF_MONTH)
            cal.set(_year, _month, _day)
            age = year - cal.get(Calendar.YEAR)
            if (month < cal.get(Calendar.MONTH) || month == cal.get(Calendar.MONTH) && date < cal.get(Calendar.DAY_OF_MONTH)) {
                --age
            }
            if (age < 0)
                throw IllegalArgumentException("Age < 0")
            return age
        }

        /**
         * method used to change date format
         *
         * @param date date
         * @return change format date
         */
        fun changeDateFormat(date: String): String {
            var date = date
            val input = SimpleDateFormat("dd/MM/yyyy")
            val output = SimpleDateFormat("MM-dd-yyyy")
            try {
                val oneWayTripDate = input.parse(date)  // parse input
                date = output.format(oneWayTripDate)    // format output
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        fun changeDateFormat(date: Date): String {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone

            Log.d("Time zone: ", tz.displayName)

            val output = SimpleDateFormat("MM-dd-yyyy hh:mm a")
            output.timeZone = tz
            try {
                return output.format(date.time)    // format output
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }

        fun changeDateFormat(timestamp: Long): String {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone

            Log.d("Time zone: ", tz.displayName)

            val output = SimpleDateFormat("MM-dd-yyyy hh:mm a")
            output.timeZone = tz
            try {
                return output.format(timestamp * 1000)    // format output
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }

        fun changeDateFormatTOMonth(timestamp: Long): String {
            val cal = Calendar.getInstance()
            /*  val tz = cal.timeZone

              Log.d("Time zone: ", tz.displayName)

              val output = SimpleDateFormat("ddMMM,yyyy")
              output.setTimeZone(tz);
              try {
                  return output.format(timestamp * 1000)    // format output
              } catch (e: ParseException) {
                  e.printStackTrace()
              }


  */


            val output = SimpleDateFormat("MMMdd,yyyy")
            try {
                return output.format(timestamp * 1000)    // format output
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * method used to change the font
         *
         * @param context get context
         * @return changed font
         */

        /**
         * method used to change the font
         *
         * @param context get context
         * @return changed font
         */

        /**
         * method used to change the font
         *
         * @param context get context
         * @return changed font
         */


        /**
         * method used to remove a comma from last in string
         *
         * @param str contain comma seprated string
         * @return string
         */
        fun method(str: String?): String? {
            var str = str
            if (str != null && str.length > 0 && str[str.length - 1] == ',') {
                str = str.substring(0, str.length - 1)
            }
            Log.d("String is" + " String", str)
            return str
        }


        @SuppressLint("NewApi")
        fun getWidth(mContext: Context): Int {
            var width = 0
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                width = size.x
            } else {
                width = display.width  // deprecated
            }
            return width
        }

        @SuppressLint("NewApi")
        fun getHeight(mContext: Context): Int {
            var height = 0
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                height = size.y
            } else {
                height = display.height  // deprecated
            }
            return height
        }


        val currentDate: String
            get() {
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("yyyy/MM/dd ")
                val strDate = mdformat.format(calendar.time)

                Log.d("Current Date:- ", strDate)

                return strDate
            }

        val dateAndTime: String
            get() {
                val c = Calendar.getInstance()
                println("Current time => " + c.time)

                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val formattedDate = df.format(c.time)

                Log.d("Current Date:- ", formattedDate)
                return formattedDate
            }

        fun getConvertedDate(oldDate: String): String {
            var date: Date? = null
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val write = SimpleDateFormat("dd-MMM-yyyy")
            write.timeZone = TimeZone.getDefault()
            try {
                date = parser.parse(oldDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return write.format(date)
        }

        fun getConvertedDateWithMonthFormat(oldDate: String): String {
            var date: Date? = null
            val parser = SimpleDateFormat("yyyy-MM-dd")
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val write = SimpleDateFormat("d MMM yyyy")
            write.timeZone = TimeZone.getDefault()
            try {
                date = parser.parse(oldDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return write.format(date)
        }

        fun getConvertedDateInSlashPatteren(oldDate: String): String {
            var date: Date? = null
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val write = SimpleDateFormat("dd/MM/yy")
            write.timeZone = TimeZone.getDefault()
            try {
                date = parser.parse(oldDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return write.format(date)
        }

        fun metersToMiles(distance: Double): Double {
            return distance / 1609.34
        }


        fun downloadFile(context: Context, url: String, filename: String) {

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val Download_Uri = Uri.parse(url)
            val request = DownloadManager.Request(Download_Uri)

            //Restrict the types of networks over which this download may proceed.
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(false)
            //Set the title of this download, to be displayed in notifications (if enabled).
            request.setTitle("Downloading")
            //Set a description of this download, to be displayed in notifications (if enabled)
            request.setDescription("Downloading File")

            request.setVisibleInDownloadsUi(true)

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            val refid = downloadManager.enqueue(request)
        }

        private fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
            val diffInMillies = date1.time - date2.time
            return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
        }


    }

}
