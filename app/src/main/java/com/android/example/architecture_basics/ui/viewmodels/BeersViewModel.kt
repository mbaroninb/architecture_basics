package com.android.example.architecture_basics.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.domain.Repository
import com.android.example.architecture_basics.helpers.BeersApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* Preparamos el viewModel para la inyección de dependencias añadiendo la etiqueta @HiltViewModel
* y poniendo @Inject contructor() después del nombre de la clase.
*
* Dentro de @Inject contructor() Hilt debe injectar el Repo. (Ver Repository)
* */
@HiltViewModel
class BeersViewModel @Inject constructor(private val repository: Repository) : ViewModel(){

    //Estado de la peticion de red
    private val _status = MutableLiveData<BeersApiStatus>()
    val status: LiveData<BeersApiStatus> = _status

    //Listado de beers a mostrar en la UI.
    private val _beers = MutableLiveData<List<BeerApi>>()
    val beers: LiveData<List<BeerApi>> = _beers

    /*
    * Este bloque init se va a ejecutar cuando el viewmodel se instancie por primera vez.
    * */
    init {
        getBeers()
    }

    /*
    * Esta funcion trae las beers desde el repositorio. Como parametro indicamos si quiere las
    * favoritas o no.
    * */
    fun getBeers( favourites:Boolean = false) {

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