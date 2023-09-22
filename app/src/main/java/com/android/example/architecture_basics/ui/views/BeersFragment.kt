package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentBeersBinding
import com.android.example.architecture_basics.helpers.BeersApiStatus
import com.android.example.architecture_basics.ui.adapters.BeersAdapter
import com.android.example.architecture_basics.ui.viewmodels.BeersViewModel
import com.android.example.architecture_basics.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //Etiqueta Hilt (ver DaggerHilt Docs en README.)
class BeersFragment : Fragment() {

    /*
    * El viewModel se crea la primera vez que el sistema llame al método onCreate() del fragment.
    * Si se recrea la UI, va a recibir la misma instancia de viewModel creada anteriormente.
    *
    * 'by viewModels()' es un delegado que internamente llama un ViewModelProvider pasandole el
    * contexto del fragment.
    * En java se veria asi:
    *   BeersViewModel model = new ViewModelProvider(this).get(BeersViewModel.class);
    *
    * Ver documentacion en README.
    * */
    private val viewModel by viewModels<BeersViewModel>()

    /*
    * Declaro un viewModel compartido para saber si debo mandar el usuario a loguear o no.
    * Es compartido porque el alcance es a nivel de activity
    * */
    private val userViewModel by activityViewModels<LoginViewModel>()

    /*
    * FragmentBeersBinding es una clase de vinculación autogenerada que contiene
    * referencias directas al XML de este fragmento.
    *
    * Ver documentacion en README.
    * */
    private var _binding: FragmentBeersBinding? = null
    private val binding get() = _binding!!

    //Variable para determinar si mostrar favoritos o no.
    private var favourites = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Declaro un observador a la propiedad loginSuccess del viewModel.(Ver LoginViewModel)
        *
        * Cuando llamo a la funcion observe le paso como parametro viewLifecycleOwner
        * que representa el ciclo de vida del fragmento para que deje de observar cuando el
        * ciclo de vida muera.
        *
        * Como el tipo de datos alojado dentro del Livedata es un Event<String>
        * (Ver clase Event dentro de helpers), lo primero que hago es verificar que no lo mostre
        * antes, y luego verifico que el usuario este logueado
        * */
        userViewModel.loginSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { loggedIn ->
                if (!loggedIn) {
                    /*
                    * Aqui se obtiene el navController declarado en la MainActivity y luego
                    * se ejecuta la accion de navegacion hacia el LoginFragment.
                    * */
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        //Verifico si cambio el check de favoritos
        binding.chkFav.setOnCheckedChangeListener { buttonView, isChecked ->
            favourites = isChecked
            viewModel.getBeers(isChecked)
        }

        // Creo el adapter y se lo asigno al RecyclerView.
        val beersAdapter = BeersAdapter {
            /*
            * Beers Adapter toma un parámetro: onItemClicked().
            * Esta función se utilizará para controlar la navegación cuando se seleccione
            * un elemento.
            *
            * En este bloque {} definimos que va a hacer la funcion.
            * Por su parte en el adapter, en onCreateViewHolder(), configuramos un onClickListener()
            * para llamar a esta funcion  pasandole el elemento que se clickeo
            * */
            val action = BeersFragmentDirections
                .actionBeersFragmentToDetailsBeerFragment(id = it.id!!, isFavorite = favourites)

            view.findNavController().navigate(action)
        }

        binding.rvBeers.adapter = beersAdapter

        /*
        * En este caso el objeto observado es una lista de Beers.
        * Cuando el valor dentro del livedata cambie, la UI va a actualizar el adapter del Recyler.
        * */
        viewModel.beers.observe(viewLifecycleOwner) { listBeers ->
            listBeers.let {
                beersAdapter.submitList(it)
            }
        }

        /*
        * Este observador va a ir mostrando los cambios de estado de la peticion de red a medida
        * que la propiedad status del viewmodel cambie.
        * Esta propiedad status es un enumerable.
        * */
        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                BeersApiStatus.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                BeersApiStatus.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }

                BeersApiStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}