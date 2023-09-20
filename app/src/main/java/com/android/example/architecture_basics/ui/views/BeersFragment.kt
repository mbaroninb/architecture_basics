package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentBeersBinding
import com.android.example.architecture_basics.helpers.BeersApiStatus
import com.android.example.architecture_basics.ui.adapters.BeersAdapter
import com.android.example.architecture_basics.ui.viewmodels.BeersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //Etiqueta Hilt (ver DaggerHilt Docs en README.)
class BeersFragment : Fragment() {

    /*
    * Al igual que en el LoginFragment instancio el viewmodel correspondiente
    * y declaro el viewBinding.
    * */
    private val viewModel by viewModels<BeersViewModel>()

    private var _binding: FragmentBeersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Configuro la toolbar para que trabaje con Navigation Components.
        val toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_parameters -> {
                    findNavController().navigate(BeersFragmentDirections.actionBeersFragmentToSettingsFragment())
                    true
                }
                else -> false
            }
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
                .actionBeersFragmentToDetailsBeerFragment(id = it.id!!)

            view.findNavController().navigate(action)
        }

        binding.rvBeers.adapter = beersAdapter

        /*
        * Mismo mecanismo que en LoginFragment.
        *
        * En este caso el objeto contenido en el livedata es una lista de Beers.
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