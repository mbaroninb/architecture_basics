package com.android.example.architecture_basics.helpers

sealed class Resource<out T>() {
    class Loading<out T>: Resource<T>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure(val exception: Exception): Resource<Nothing>()
}
