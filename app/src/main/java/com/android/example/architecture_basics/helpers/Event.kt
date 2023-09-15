package com.android.example.architecture_basics.helpers

//Codigo de StackOverflow -
// Revisar https://stackoverflow.com/questions/53484781/android-mvvm-is-it-possible-to-display-a-message-toast-snackbar-etc-from-the
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