package com.android.example.architecture_basics.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.architecture_basics.helpers.Event

class LoginViewModel : ViewModel() {

    /*
    * Ver aclaracion al final del archivo "Propiedad copia de seguridad"
    *
    * Dentro de estas propiedades encontramos los tipos LiveData y MutableLiveData
    * Estos tipos livedata van a notificar a los observadores que tengan vinculados cada vez que el
    * valor dentro de livedata cambie.
    * (Ver documentacion de ViewModel & Livedata en README)
    * */
    private val _loginFailedMessage = MutableLiveData<Event<String>>()
    val loginFailedMessage: LiveData<Event<String>>
        get() = _loginFailedMessage


    private val _loginSuccess = MutableLiveData(Event(false))
    val loginSuccess: LiveData<Event<Boolean>>
        get() = _loginSuccess

    /*
    * En esta funcion simple simulo un login.
    * En un caso real deberia llamar a un servicio que se loguee contra el ERP por ej.
    *
    * No retorno directamente un booleano en la funcion porque en caso de que llamemos a un
    * servicio, este tendria un tiempo de retardo y terminaria bloqueando la UI
    * que va a estar esperando el retorno de la funcion.
    * */
    fun logIn(user: String, pass: String) {
        val usuario = "admin"
        val password = "1234"

        if (usuario == user && password == pass) {
            _loginSuccess.value = Event(true)
        } else {
            _loginSuccess.value = Event(false)
            _loginFailedMessage.value = Event("Falló el login")
        }

    }


}

/*
"Propiedad de copia de seguridad"
Se utiliza para que los valores puedan ser observados pero no modificados fuera del viewmodel.

    // Propiedad mutable privada que solo se puede modificar dentro de la clase se declara.
    private var _count = 0


    // Propiedad pública inmutable.
    // Cuando se accede a la propiedad, se llama a la función get() y // se devuelve el valor de _count.
    val count: Int
       get() = _count

*/