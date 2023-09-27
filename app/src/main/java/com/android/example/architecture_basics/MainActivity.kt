package com.android.example.architecture_basics

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.example.architecture_basics.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/*
* @AndroidEntryPoint indica a Hilt que puede proporcionar dependencias en esta clase.
* */
@AndroidEntryPoint //Etiqueta Hilt (ver DaggerHilt Docs en README.)
class MainActivity : AppCompatActivity() {

    //(ViewBinding) Ver documentacion en README.
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    /*
    * Declaro el controlador de navegacion con la "promesa" (lateinit) de inicializarlo despues.
    * */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        /*
        * Creo una variable navHostFragment que va a contener el contenedor de fragmentos y luego
        * le asigno este controlador de fragmentos a el navController.
        *
        * Este navHostFragment es un FragmentContainerView declarado en el xml de la activity.
        *
        * Dentro de este "host de fragmentos" se va a realizar la navegacion de las diferentes
        * pantallas de la aplicacion.
        * Esto significa que nuestra aplicacion va a ser Single Activity App
        *
        * Ver Navigation Components en README.
        * */
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupNavigationRailMenu(navController)
        setupBottomNavMenu(navController)

        /*
        * Listener que escucha cuando se inicia el Login para esconder la barra de tareas.
        * */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationBar?.visibility = View.GONE
                    binding.navigationRailBar?.visibility = View.GONE

                }

                else -> {
                    binding.bottomNavigationBar?.visibility = View.VISIBLE
                    binding.navigationRailBar?.visibility = View.VISIBLE
                }
            }
        }
    }

    /*
    * Seteo el NavigationRailMenu si este existe.
    * Esto es asi porque tengo dos layouts para MainActivity, cada uno con su barra correspondiente.
    * */
    private fun setupNavigationRailMenu(navController: NavController) {
        val sideNavView = binding.navigationRailBar
        sideNavView?.setupWithNavController(navController)
    }

    //Seteo el BottomNavMenu si este existe.
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavigationBar
        bottomNav?.setupWithNavController(navController)
    }
}