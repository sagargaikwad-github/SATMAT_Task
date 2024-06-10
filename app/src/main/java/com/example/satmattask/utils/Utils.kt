package com.example.satmattask.utils

import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.satmattask.Interface.OnDateSelectedListener
import com.example.satmattask.R
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    private var dialog: AlertDialog? = null
    var authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MTc1NjQzNjksImV4cCI6MTcxODg2MDM2OSwiaXNzIjoiaHR0cHM6XC9cL20tcGUuaW4iLCJkYXRhIjp7ImN1c19pZCI6bnVsbCwiY3VzX25hbWUiOm51bGwsImN1c19tb2JpbGUiOiI3MDMwNTEyMzQ2IiwiY3VzX3Bhc3N3b3JkIjoiOTQ3MTgifX0.gEq3VnWyB866w31vRu47x2U-I3DAf9IiiELQbMmxC9Y"

    fun showDialog(context: android.content.Context) {
        dialog =
            AlertDialog.Builder(context).setView(R.layout.custom_alertdialog).setCancelable(false)
                .create()
        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    // val BASE_URL = "https://supay.in/";
    val BASE_URL = "https://m-pe.in/";

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val httpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofitInstance = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }


    //DatePicker dialog
    fun showDatePickerDialog(
        context: Context,
        listener: OnDateSelectedListener,
        whichDate: String,
        currentSelectedDate: String
    ) {
        val calendar = Calendar.getInstance()

        val dateFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val date = sdf.parse(currentSelectedDate)
        calendar.timeInMillis = date.time

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialogTheme,
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val month = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val monthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val selectedDate = "$month/$monthOfYear/$year"

                listener.onDateSelected(selectedDate, whichDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun stringToDate(dateString: String): Date {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.parse(dateString) ?: Date()
    }

    fun stringToDateSlashDate(dateString: String): Date {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val stringDate = outputFormat.format(date)
        return outputFormat.parse(stringDate) ?: Date()
    }
}