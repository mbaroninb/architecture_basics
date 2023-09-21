package com.android.example.architecture_basics.domain

import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.toEntity
import com.android.example.architecture_basics.data.network.BeersApiService
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.data.network.models.toApi
import javax.inject.Inject

/*
* Dentro de @Inject contructor() Hilt debe injectar el/los Daos de la BBDD y el apiService
* */
class Repository @Inject constructor(
    private val beerDao: BeerDao,
    private val apiService: BeersApiService
) {

    //Busca datos del servicio web o de la base de datos.
    suspend fun fetchBeers( isFavourite: Boolean): List<BeerApi> {
        return if (isFavourite) {
            /*
            * Obtengo las Beers desde la base de datos, pero antes de enviarlas las mapeo a BeerApi para
            * poder reutilizar el codigo del viewmodel.
            * */
            beerDao.getAllBeers().map { it.toApi() }
        } else {
            // Obtengo del servicio web. punkApi (retrofit)
            apiService.getBeers()
        }
    }

    /*
    * Obtiene un Beer por ID de la api o de la base de datos si es un Favorito.
    * */
    suspend fun fetchBeerById(id: Int, isFavourite: Boolean): BeerApi {
        return if (isFavourite) {
            /*
            * A efectos de reutilizar esta pantalla para ver los favoritos, convierto los datos
            * obtenidos de la BBDD (BeerEntity) en BeerApi mediante una funcion de extencion
            * declarada en BeerApi.
            * */
            beerDao.getBeerById(id).toApi()
        } else {
            //Obtiene solo un dato del servicio web. punkApi (retrofit)
            apiService.getBeerById(id)[0]
        }
    }




    /*
    * En esta funcion recibo por parametros un objeto BeerApi y lo guardo en la base de datos
    * como favoritos.
    *
    * Para poder almacenar el objeto recibido, necesito parsearlo a un tipo BeerEntity (tabla).
    * Esto lo hago ejecutando la funcion de extencion toEntity() declarada en BeerEntity, que
    * transforma un BeerApi en un BeerEntity.
    *
    * Una vez parseado el objeto lo mando a insertar a la BBDD por medio del Dao
    * */
    suspend fun saveFavourite(beer: BeerApi): Long {

        return beerDao.insertBeer(beer.toEntity())
    }



}