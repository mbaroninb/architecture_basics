package com.android.example.architecture_basics.di

import com.android.example.architecture_basics.data.network.BeersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/*
* Hilt nos permite crear módulos cuando necesitemos inyectar clases no son de nuestra propiedad
* porque proviene de una biblioteca externa (clases como Retrofit, OkHttpClient
* o bases de datos de Room).
*
* Para esto necesitamos la etiqueta @Module que indica que es un modulo de Hilt y una segunda
* etiqueta @IntallIn(), que va a definir el alcance de nuestras dependencias. (osea que Hilt
* creara una instancia y esta no morirá hasta que se salga del alcance definido)
*
* El alcance de nuestro módulo es SingletonComponent::class (No confundir con el patrón de diseño)
* por lo que las instancias que creemos no morirán hasta que muera la app.
* */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.punkapi.com/v2/"

    /*
    * @Provide indica a Hilt cómo proporcionar una instancia mediante una función dentro del módulo,
    * En este caso de tipo Retrofit.
    *
    * @Singleton indica que queremos que se mantenga una única instancia de la clase en el proyecto.
    *
    * */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    /*
    * En la siguiente funcion provideApiService() podemos pasar como parametro una instancia
    * de Retrofit, porque ya indicamos a Hilt como inyectarla en la funcion provideRetrofit()
    * */
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): BeersApiService = retrofit.create(BeersApiService::class.java)

}


