package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.example.architecture_basics.databinding.FragmentLoginBinding
import com.android.example.architecture_basics.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    /*
    * El viewModel se crea la primera vez que el sistema llame al método onCreate() del fragment.
    * Si se recrea el fragmento, va a recibir la misma instancia de viewModel creada anteriormente.
    *
    * 'by viewModels()' es un delegado que internamente llama un ViewModelProvider pasandole el
    * contexto del fragment.
    * En java se veria asi:
    *   DiceRollViewModel model = new ViewModelProvider(this).get(DiceRollViewModel.class);
    *
    * Ver documentacion en README.
    * */
    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val userText = binding.txtInputUser.editText?.text.toString()
            val passText = binding.textInputPassword.editText?.text.toString()

            viewModel.logIn(userText, passText)
        }

        viewModel.loginFailedMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { permission ->
                if (permission) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
                }
            }
        }

    }


}