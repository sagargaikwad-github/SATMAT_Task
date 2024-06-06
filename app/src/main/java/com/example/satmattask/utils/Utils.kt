package com.example.satmattask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.satmattask.R
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
}