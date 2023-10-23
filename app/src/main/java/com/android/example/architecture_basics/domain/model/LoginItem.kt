package com.android.example.architecture_basics.domain.model

import androidx.annotation.DrawableRes

//Objeto de datos del carousell del login.
data class LoginItem(
    val title: String,
    @DrawableRes val icon: Int
)