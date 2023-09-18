package com.android.example.architecture_basics

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
* @HiltAndroidApp indica que es una clase base para tu aplicación
* que sirve como contenedor de dependencia a nivel de la aplicación.(ver DaggerHilt Docs en README.)
* */
@HiltAndroidApp
class BaseApplication: Application()