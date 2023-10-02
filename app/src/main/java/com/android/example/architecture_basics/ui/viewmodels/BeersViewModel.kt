package com.android.example.architecture_basics.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.data.repository.Repository
import com.android.example.architecture_basics.domain.model.BeerDomain
import com.android.example.architecture_basics.domain.helpers.BeersApiStatus
import com.android.example.architecture_basics.domain.helpers.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* Preparamos el viewModel para la inyección de dependencias añadiendo la etiqueta @HiltViewModel
* y poniendo @Inject contructor() después del nombre de la clase.
*
* Dentro de @Inject contructor() Hilt debe injectar el Repo. (Ver Repository)
* */
@HiltViewModel
class BeersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //Estado de la peticion de red
    private val _status = MutableLiveData<BeersApiStatus>()
    val status: LiveData<BeersApiStatus> = _status

    //Listado de beers a mostrar en la UI.
    private val _beers = MutableLiveData<List<BeerDomain>>()
    val beers: LiveData<List<BeerDomain>> = _beers

    private var _currentBeer: MutableLiveData<BeerDomain> = MutableLiveData<BeerDomain>()
    val currentBeer: LiveData<BeerDomain>
        get() = _currentBeer


    /*
    * Esta funcion trae las beers desde el repositorio. Como parametro indicamos si quiere las
    * favoritas o no.
    * */
    fun getBeers(favourites: Boolean = false) {

        /*
        * Como la consulta de red es un proceso costoso en tiempos, esto se realiza en una
        * corrutina.
        *
        * El viewModelScope es el alcance de la corrutina.
        * Cualquier rutina lanzada en este ámbito se cancela automáticamente si se borra el ViewModel
        *
        * Ver Corrutinas Docs en README.
        * Ver comentarios al final del documento.
        * */
        viewModelScope.launch {
            _status.value = BeersApiStatus.LOADING
            try {
                _beers.value = repository.fetchBeers(favourites)
                _status.value = BeersApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BeersApiStatus.ERROR
                _beers.value = listOf()
            }
        }
    }

    fun updateCurrentBeer(beer: BeerDomain) {
        _currentBeer.value = beer
    }

    //Mensaje de favorito
    private val _favouriteMessage = MutableLiveData<Event<String>>()
    val favouriteMessage: LiveData<Event<String>> = _favouriteMessage


    fun saveFavourite() {
        viewModelScope.launch {
            try {
                _favouriteMessage.value = if (repository.saveFavourite(currentBeer.value!!) >= 1L) {
                    Event("Agregado a favoritos")
                } else {
                    Event("No se agrego a favoritos")
                }


            } catch (e: Exception) {
                _favouriteMessage.value = Event("Falló -> ${e.message}")
            }
        }
    }

    fun removeFavourite() {
        viewModelScope.launch {
            try {
                _favouriteMessage.value = if (repository.removeFavourite(currentBeer.value!!) > 0) {
                    Event("Quitado de favoritos")
                } else {
                    Event("No se quito de favoritos")
                }


            } catch (e: Exception) {
                _favouriteMessage.value = Event("Falló -> ${e.message}")
            }
        }
    }

    fun checkFavourite(id: Int) = liveData(Dispatchers.IO) {
        try {
            emit(repository.checkFavourite(id))
        } catch (e: Exception) {
            emit(false)
            _favouriteMessage.value = Event("Falló -> ${e.message}")
        }
    }

}

/*
* El Dispatcher predeterminado del bloque viewModelScope.launch{} es el Dispatcher.Main, que
* corresponde al hilo principal.
*
* Aun asi, la llamada a Retrofit funciona sin bloquear el hilo principal debido a que la
* implementación de "suspend" en la llamada de Retrofit delega en Call<T>.enqueue.
* Esto significa que ya se ejecuta en en segundo plano de forma predeterminada con su ejecutor,
* en lugar de utilizar el Dispatcher especificado en el bloque launch (Dispatcher.Main).
*
* En caso de realizar otra tarea costosa que requiera de un Dispatcher.IO, puede implementarse el
* bloque de la siguiente manera:
*
   viewmodelScope.launch(Dispatchers.IO) {
        TO_DO("Heavy work")
    }
*
* */