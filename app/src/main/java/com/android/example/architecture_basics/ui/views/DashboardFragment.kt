package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentDashboardBinding
import com.android.example.architecture_basics.helpers.MarsApiStatus
import com.android.example.architecture_basics.ui.adapters.PhotoGridAdapter
import com.android.example.architecture_basics.ui.viewmodels.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //Etiqueta de Dagger Hilt
class DashboardFragment : Fragment() {

    /*
    * Al igual que en el LoginFragment instancio el viewmodel correspondiente
    * y declaro el viewBinding.
    * */

    private val viewModel by viewModels<DashboardViewModel>()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creo el adapter y se lo asigno al RecyclerView que contiene las imagenes.
        val photoAdapter = PhotoGridAdapter()
        binding.rvPhotosGrid.adapter = photoAdapter

        /*
        * Mismo mecanismo que en LoginFragment.
        *
        * En este caso el objeto contenido en el livedata es una lista de MarsPhotos.
        * Cuando el valor dentro del livedata cambie, la UI va a actualizar el adapter del Recyler.
        * */
        viewModel.photos.observe(viewLifecycleOwner) { listMarsPhoto ->
            listMarsPhoto.let {
                photoAdapter.submitList(it)
            }
        }

        /*
        * Este observador va a ir mostrando los cambios de estado de la peticion de red a medida
        * que la propiedad status del viewmodel cambie.
        * Esta propiedad status es un enumerable.
        * */
        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                MarsApiStatus.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                MarsApiStatus.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }

                MarsApiStatus.DONE -> {
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