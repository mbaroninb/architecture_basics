package com.android.example.architecture_basics.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.data.network.models.toApi
import com.android.example.architecture_basics.domain.Repository
import com.android.example.architecture_basics.helpers.BeersApiStatus
import com.android.example.architecture_basics.helpers.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsBeerViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    //Estado de la peticion de red
    private val _status = MutableLiveData<BeersApiStatus>()
    val status: LiveData<BeersApiStatus> = _status

    //Listado de beers a mostrar en la UI.
    private val _beer = MutableLiveData<BeerApi>()
    val beer: LiveData<BeerApi> = _beer


    fun getBeerById(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            _status.value = BeersApiStatus.LOADING
            try {
                _beer.value = repository.fetchBeerById(id, isFavourite)
                _status.value = BeersApiStatus.DONE
            } catch (e: Exception) {
                _status.value = BeersApiStatus.ERROR
                _beer.value = BeerApi()
            }
        }
    }


    //Mensaje de favorito
    private val _favouriteMessage = MutableLiveData<Event<String>>()
    val favouriteMessage: LiveData<Event<String>> = _favouriteMessage

    fun saveFavourite() {
        viewModelScope.launch {
            try {
                _favouriteMessage.value = if (repository.saveFavourite(beer.value!!) == 1L) {
                    Event("Agregado a favoritos")
                } else {
                    Event("No se agrego a favoritos")
                }


            } catch (e: Exception) {
                _favouriteMessage.value = Event("Falló -> ${e.message}")
            }
        }
    }
}