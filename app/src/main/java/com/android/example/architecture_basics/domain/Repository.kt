package com.android.example.architecture_basics.domain

import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.toEntity
import com.android.example.architecture_basics.data.network.BeersApiService
import com.android.example.architecture_basics.domain.model.BeerDomain
import com.android.example.architecture_basics.domain.model.toDomain
import javax.inject.Inject

/*
* Dentro de @Inject contructor() Hilt debe injectar el/los Daos de la BBDD y el apiService
* */
class Repository @Inject constructor(
    private val beerDao: BeerDao,
    private val apiService: BeersApiService
) {

    //Busca datos del servicio web o de la base de datos.
    suspend fun fetchBeers( isFavourite: Boolean): List<BeerDomain> {
        return if (isFavourite) {
            /*
            * Obtengo las Beers desde la base de datos, pero antes de enviarlas las mapeo a
            * BeerDomain para poder reutilizar el codigo del viewmodel.
            * */
            beerDao.getAllBeers().map { it.toDomain() }
        } else {
            // Obtengo del servicio web. punkApi (retrofit)
            apiService.getBeers().map { it.toDomain() }
        }
    }

    /*
    * En esta funcion recibo por parametros un objeto BeerDomain y lo guardo en la base de datos
    * como favoritos.
    *
    * Para poder almacenar el objeto recibido, necesito parsearlo a un tipo BeerEntity (tabla).
    * Esto lo hago ejecutando la funcion de extencion toEntity() declarada en BeerEntity, que
    * transforma un BeerDomain en un BeerEntity.
    *
    * Una vez parseado el objeto lo mando a insertar a la BBDD por medio del Dao
    * */
    suspend fun saveFavourite(beer: BeerDomain): Long {
        return beerDao.insertBeer(beer.toEntity())
    }

    suspend fun removeFavourite(beer: BeerDomain): Int {
        return beerDao.deleteBeer(beer.toEntity())
    }

    suspend fun checkFavourite(id: Int): Boolean{
        return beerDao.checkFavouriteExists(id)
    }


}