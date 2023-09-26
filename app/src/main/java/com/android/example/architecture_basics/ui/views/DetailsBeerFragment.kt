package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentDetailsBeerBinding
import com.android.example.architecture_basics.ui.viewmodels.BeersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsBeerFragment : Fragment() {

    private val viewModel by activityViewModels<BeersViewModel>()


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
        /*
        * Observa el valor currentBeer del viewmodel compartido, y actuliza los datos cuando cambie.
        * */
        viewModel.currentBeer.observe(viewLifecycleOwner) {
            it?.let {
                binding.txtNoData.visibility = View.GONE
                binding.dataContainer.visibility = View.VISIBLE

                val imgUri = it.imageUrl.toUri().buildUpon().scheme("https").build()
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


                viewModel.checkFavourite(it.id).observe(viewLifecycleOwner) { result ->
                    binding.chkFav.isChecked = result
                }

            }
        }

        binding.chkFav.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed && isChecked) {
                viewModel.saveFavourite()
            } else if (checkBox.isPressed && !isChecked) {
                viewModel.removeFavourite()
            }
        }

        viewModel.favouriteMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { favMessage ->
                Toast.makeText(requireContext(), favMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

