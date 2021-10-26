package ar.com.example.chatExample.core

sealed class Response<out T>{

    class Loading<out T> : Response<T>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val exception: Exception): Response<Nothing>()



}
