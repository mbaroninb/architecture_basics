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

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel by viewModels<DashboardViewModel>()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("rendimiento","onCreateView DashFragment")

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoAdapter = PhotoGridAdapter()

        binding.photosGrid.adapter = photoAdapter

        viewModel.photos.observe(viewLifecycleOwner) { listPhotos->
            listPhotos.let { photo ->
                photoAdapter.submitList(photo)
            }
        }

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

        Log.d("rendimiento","onDestroyView DashFragment")

        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("rendimiento","onDestroy DashFragment")

    }
}