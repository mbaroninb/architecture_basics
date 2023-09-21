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

    /*
    * FragmentLoginBinding es una clase de vinculación autogenerada que contiene
    * referencias directas al XML de este fragmento.
    *
    * Ver documentacion en README.
    * */
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflo la vista y retorno la raiz de esa vista (xml)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        * Declaro un observador a la propiedad loginFailedMessage del viewModel.(Ver LoginViewModel)
        *
        * Cuando llamo a la funcion observe le paso como parametro viewLifecycleOwner
        * que representa el ciclo de vida del fragmento para que deje de observar cuando el
        * ciclo de vida muera.
        *
        * Como el tipo de datos alojado dentro del Livedata es un Event<String>
        * (Ver clase Event dentro de helpers), lo primero que hago es verificar que no lo mostre
        * antes, y luego tomo el mensaje (String) y lo muestro en el toast.
        * */
        viewModel.loginFailedMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        /*
        * Mismo proceso que en loginFailedMessage.
        *
        * Cuando el login sea correcto se ejecuta el contenido en la funcion lambda let().
        * */
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { permission ->
                if (permission) {
                    /*
                    * Aqui se obtiene el navController declarado en la MainActivity y luego
                    * se ejecuta la accion de navegacion hacia el BeersFragment. Este destino esta
                    * declarado en el grafo de navegacion (nav_graph.xml).
                    * */
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBeersFragment())
                }
            }
        }

    }


}