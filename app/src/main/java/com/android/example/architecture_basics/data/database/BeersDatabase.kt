package com.android.example.architecture_basics.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.BeerEntity

@Database(
    entities = [BeerEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BeersDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao

}