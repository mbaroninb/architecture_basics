package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentBeersBinding
import com.android.example.architecture_basics.databinding.FragmentDetailsBeerBinding
import com.android.example.architecture_basics.helpers.BeersApiStatus
import com.android.example.architecture_basics.ui.viewmodels.BeersViewModel
import com.android.example.architecture_basics.ui.viewmodels.DetailsBeerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsBeerFragment : Fragment() {

    val args: DetailsBeerFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailsBeerViewModel>()

    private var _binding: FragmentDetailsBeerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBeerById(args.id)

        viewModel.beer.observe(viewLifecycleOwner){
            it.let {
                val imgUri = it.imageUrl!!.toUri().buildUpon().scheme("https").build()
                binding.ivArticulo.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
                binding.txtName.text = it.name
                binding.txtTag.text = it.tagline
                binding.txtDescription.text = it.description
                binding.txtFirsBrewed.text = it.firstBrewed
                binding.txtIbu.text = it.ibu.toString()
                binding.txtAbv.text = "${it.abv}%"
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                BeersApiStatus.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                BeersApiStatus.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.dataContainer.visibility = View.GONE
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }

                BeersApiStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

        binding.btnFav.setOnClickListener{

            Toast.makeText(requireContext(),"Favorito",Toast.LENGTH_SHORT).show()
        }

    }

}