package com.android.example.architecture_basics.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.data.network.BeersApiService
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.helpers.BeersApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsBeerViewModel @Inject constructor(private val beersApiService: BeersApiService) :
    ViewModel() {

    //Estado de la peticion de red
    private val _status = MutableLiveData<BeersApiStatus>()
    val status: LiveData<BeersApiStatus> = _status

    //Listado de beers a mostrar en la UI.
    private val _beer = MutableLiveData<BeerApi>()
    val beer: LiveData<BeerApi> = _beer


    fun getBeerById(id: Int) {
        viewModelScope.launch {
            _status.value = BeersApiStatus.LOADING
            try {
                beersApiService.getBeerById(id)[0].also { _beer.value = it }
                _status.value = BeersApiStatus.DONE
            } catch (e: Exception) {
                _status.value = BeersApiStatus.ERROR
                _beer.value = BeerApi()
            }
        }
    }
}