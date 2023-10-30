package com.android.example.architecture_basics.ui.views


import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.example.architecture_basics.databinding.FragmentSettingsBinding
import com.android.example.architecture_basics.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnExportDb.setOnClickListener {

            viewModel.exportLogAndDB()
            Toast.makeText(requireContext(), "Exportar db aqui!", Toast.LENGTH_SHORT).show()
        }

        viewModel.zipUri.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {zipUri->
                sendEmail(zipUri)
            }
        }

    }

    private fun sendEmail(attachment: Uri) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        //Set type to email
        emailIntent.type = "vnd.android.cursor.dir/email"
        val toEmail = "whatever@gmail.com"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, toEmail)
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachment)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Data for Training Log")
        emailIntent.clipData = ClipData.newRawUri("", attachment)
        emailIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        activity?.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }

}