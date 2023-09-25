package com.android.example.architecture_basics.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.example.architecture_basics.data.database.entity.BeerEntity

@Dao
interface BeerDao {

    @Query("SELECT * FROM beers")
    suspend fun getAllBeers(): List<BeerEntity>

    @Query("SELECT EXISTS(SELECT * FROM beers WHERE id = :id)")
    suspend fun checkFavouriteExists(id : Int) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer: BeerEntity): Long

    @Delete
    suspend fun deleteBeer(beer: BeerEntity): Int
}