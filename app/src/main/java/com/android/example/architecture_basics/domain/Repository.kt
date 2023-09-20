package com.android.example.architecture_basics.domain

import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.toEntity
import com.android.example.architecture_basics.data.network.BeersApiService
import com.android.example.architecture_basics.data.network.models.BeerApi
import javax.inject.Inject

/*
* Dentro de @Inject contructor() Hilt debe injectar el/los Daos de la BBDD y el apiService
* */
class Repository @Inject constructor(
    private val beerDao: BeerDao,
    private val apiService: BeersApiService
) {

    //Busca datos del servicio web. punkApi (retrofit)
    suspend fun fetchApiBeers(): List<BeerApi> {
        return apiService.getBeers()
    }

    //Obtiene solo un dato del servicio web. punkApi (retrofit)
    suspend fun fetchApiBeerById(id: Int): BeerApi {
        return apiService.getBeerById(id)[0]
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
        beerDao.insertBeer(beer.toEntity())
        return 1 ///CORREGIR
    }

    /*
    * Mismo proceso que en saveFavourites() pero con la operacion Delete.
    * */
    suspend fun removeFavourite(beer: BeerApi) {
        return beerDao.deleteBeer(beer.toEntity())
    }
}