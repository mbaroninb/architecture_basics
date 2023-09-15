package com.android.example.architecture_basics.helpers

/*
* Codigo de StackOverflow
* https://stackoverflow.com/questions/53484781/android-mvvm-is-it-possible-to-display-a-message-toast-snackbar-etc-from-the
*
* Esta clase va a ser un "contenedor" generico de datos.
* Ocupo esta clase porque con ella puedo evitar que el observador de un Livedata se vuelva a
* disparar cuando la vista (activity/fragment) pase por el metodo onResume.
*
* Principalmente la uso al querer mostrar mensajes generados en el viewmodel, en un Toast de la UI
*/
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}