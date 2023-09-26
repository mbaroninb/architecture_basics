package com.android.example.architecture_basics.di

import android.content.Context
import androidx.room.Room
import com.android.example.architecture_basics.data.database.BeersDatabase
import com.android.example.architecture_basics.helpers.GlobalConst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
* Modulo para inyeccion de base de datos.
*
* Es muy similar a NetworkModule. Ver README.
* */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context : Context) =
        Room.databaseBuilder(
            context,
            BeersDatabase::class.java,
            GlobalConst.DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun providesBeerDao(db: BeersDatabase) = db.beerDao()
}