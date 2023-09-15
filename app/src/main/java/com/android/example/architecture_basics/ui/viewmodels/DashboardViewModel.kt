package com.android.example.architecture_basics.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.data.network.MarsApiService
import com.android.example.architecture_basics.data.network.models.MarsPhoto
import com.android.example.architecture_basics.helpers.MarsApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel
@Inject constructor(private val marsApiService: MarsApiService) : ViewModel() {

    //Estado de la peticion de red
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus> = _status

    //Listado de imagenes a mostrar en la UI.
    private val _photos = MutableLiveData<List<MarsPhoto>>()
    val photos: LiveData<List<MarsPhoto>> = _photos

    /*
    * Este bloque init se va a ejecutar cuando el viewmodel se instancie por primera vez.
    * */
    init {
        getMarsPhotos()
    }

    /*
    * Esta funcion trae las imagenes desde un servicio web marsApiService (retrofit)
    * */
    private fun getMarsPhotos() {

        /*
        * Como la consulta de red es un proceso costoso en tiempos, esto se realiza en una
        * corrutina.
        *
        * El viewModelScope es el alcance de la corrutina.
        * Cualquier rutina lanzada en este ámbito se cancela automáticamente si se borra el ViewModel
        *
        * */
        viewModelScope.launch() {
            _status.value = MarsApiStatus.LOADING
            try {
                _photos.value = getPhotos()!!
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }


    private suspend fun getPhotos() = withContext(Dispatchers.IO) { marsApiService.getPhotos() }
}


