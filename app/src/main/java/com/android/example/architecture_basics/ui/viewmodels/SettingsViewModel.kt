package com.android.example.architecture_basics.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.domain.helpers.Event
import com.android.example.architecture_basics.domain.helpers.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val util:Util
) : ViewModel() {

    private val _zipUri: MutableLiveData<Event<Uri>> = MutableLiveData()
    val zipUri: LiveData<Event<Uri>>
        get() = _zipUri

    fun exportLogAndDB() {
        viewModelScope.launch(Dispatchers.Default){
             util.exportarLogYDB()?.let { _zipUri.postValue(Event(it))  }
        }
    }
}