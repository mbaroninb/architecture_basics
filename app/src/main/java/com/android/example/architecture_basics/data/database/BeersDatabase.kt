package com.android.example.architecture_basics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.BeerEntity


/*
* Clase que define la configuración de la base de datos y sirve como el punto de acceso principal
* de la app a los datos persistentes
* */
@Database(
    entities = [BeerEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BeersDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao

}