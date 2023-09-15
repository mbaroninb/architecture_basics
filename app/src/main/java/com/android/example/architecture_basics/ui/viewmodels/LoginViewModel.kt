package com.android.example.architecture_basics.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.architecture_basics.helpers.Event


class LoginViewModel : ViewModel() {

    private val _loginFailedMessage  = MutableLiveData<Event<String>>()
    val loginFailedMessage  : LiveData<Event<String>>
        get() = _loginFailedMessage

    private val _loginSuccess = MutableLiveData(Event(false))
    val loginSuccess  : LiveData<Event<Boolean>>
        get() = _loginSuccess

    /*
    * En esta funcion simple simulo un login.
    * En un caso real deberia llamar a un servicio que se loguee contra el ERP por ej.
    * */
    fun logIn(user: String, pass:String){
        val usuario = "admin"
        val password = "1234"

        if (usuario == user && password == pass){
            _loginSuccess.value = Event(true)
        }
        else {
            _loginSuccess.value = Event(false)
            _loginFailedMessage.value = Event("Falló el login")
        }

    }


}