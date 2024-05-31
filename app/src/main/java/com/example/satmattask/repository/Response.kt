package com.example.satmattask.repository

sealed class Response<T>(val data: T? = null, val error: T? = null) {
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(errorData: T? = null) : Response<T>(error = errorData)

}