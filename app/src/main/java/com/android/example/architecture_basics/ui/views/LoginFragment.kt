package com.android.example.architecture_basics.ui.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.android.example.architecture_basics.databinding.FragmentLoginBinding
import com.android.example.architecture_basics.ui.viewmodels.LoginViewModel


class LoginFragment : Fragment() {


    /*
    * Al igual que en el BeersFragment instancio el viewmodel correspondiente
    * y declaro el viewBinding.
    * */
    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var savedStateHandle: SavedStateHandle

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Advertencia")
                        .setMessage("Debe iniciar sesion para continuar. ¿Desea salir?")
                        .setPositiveButton("Si") { _, _ ->
                            requireActivity().finish()
                        }
                    alertDialogBuilder.setNegativeButton("No") { _, _ -> }
                    alertDialogBuilder.show()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Se obtiene el estado de la pila de navegacion anterior y se almacena informacion del login
        * en un savedStateHandler para que pueda ser recuperada posteriormente cuando se vuelva en
        * la pila
        * */
        //savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        //savedStateHandle.set(LOGIN_SUCCESSFUL, false)


        /*
        * En el metododo setOnClickListener del boton de login, obtengo de la UI los datos
        * ingresados por el usuario y se los paso al viewmodel llamando a la funcion logIn.
        * */
        binding.btnLogin.setOnClickListener {
            val userText = binding.txtInputUser.editText?.text.toString()
            val passText = binding.textInputPassword.editText?.text.toString()

            viewModel.logIn(userText, passText)
        }


        /*
        * Cuando el login sea correcto se ejecuta el contenido en la funcion lambda let{}.
        * */
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { loggedIn ->
                if (loggedIn) {
                    //savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.loginFailedMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), "Fallo el login", Toast.LENGTH_LONG).show()
            }
        }

    }


}