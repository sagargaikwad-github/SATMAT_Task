package com.example.satmattask.repository

sealed class ResponseSealed<T>(val data: T? = null, val error: T? = null) {
    class Success<T>(data: T? = null) : ResponseSealed<T>(data = data)
    class Error<T>(errorData: T? = null) : ResponseSealed<T>(error = errorData)

}