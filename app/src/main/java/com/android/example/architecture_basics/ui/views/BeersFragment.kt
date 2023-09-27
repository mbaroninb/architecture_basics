package com.android.example.architecture_basics.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.databinding.FragmentBeersBinding
import com.android.example.architecture_basics.helpers.BeersApiStatus
import com.android.example.architecture_basics.helpers.BeersListOnBackPressedCallback
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
    * 'by activityViewModels()' es un delegado que internamente llama un ViewModelProvider pasandole el
    * contexto de la actividad que hostea el fragment.
    *
    * Este viewmodel es compartido, ya que sera usado por el fragmento DetailsBeer.
    *
    * Ver documentacion en README.
    * */
    private val viewModel by activityViewModels<BeersViewModel>()

    /*
    * Declaro un viewModel compartido con LoginFragment para saber si debo mandar el usuario
    * a loguear o no.
    * */
    private val loginViewModel by activityViewModels<LoginViewModel>()

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

    //Callback para manejo custom de boton back
    private lateinit var callback: BeersListOnBackPressedCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Aqui observamos el valor LOGIN_SUCCESSFUL almacenado en SavedStateHandle en LoginFragment.
        * Este valor lo obtenemos del BackStack.
        * Si el valor es true, obtenemos los datos del servidor, de lo contrario se podrá finalizar
        * la actividad -> requireActivity().finish()
        * */
        val currentBackStackEntry = findNavController().currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry){ success ->
                if (success) {
                    viewModel.getBeers()
                }else{
                    requireActivity().finish()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflo la vista y retorno la raiz de esa vista (xml)
        _binding = FragmentBeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Si los datos del login son false, se redirecciona al LoginFragment
        * */
        loginViewModel.loginSuccess.observe(viewLifecycleOwner) {
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
        binding.chkFav.setOnCheckedChangeListener { _, isChecked ->
            favourites = isChecked
            viewModel.getBeers(isChecked)
        }

        /*
        * Creo el adapter y se lo asigno al RecyclerView.
        * Beers Adapter toma un callback como parámetro: onItemClicked().
        * Esta función se utilizará para controlar la navegación cuando se seleccione
        * un elemento.
        *
        * En este bloque {} definimos que va a hacer la funcion.
        * Por su parte en el adapter, en onCreateViewHolder(), configuramos un onClickListener()
        * para llamar a esta funcion  pasandole el elemento que se clickeo.
        * */
        val beersAdapter = BeersAdapter {
            viewModel.updateCurrentBeer(it)
            binding.slidingPaneLayout.openPane()
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
            status?.let{
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

        //Obtengo el slidingPanelLayout y lo conecto con el sistema de back button.
        val slidingPaneLayout = binding.slidingPaneLayout
        callback = BeersListOnBackPressedCallback(slidingPaneLayout)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        //Bloqueo el Panel para que no pueda deslizarse
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
    }

    override fun onResume() {
        super.onResume()
        callback.onResumedCallback()
    }

    override fun onPause() {
        super.onPause()
        callback.onPausedCallback()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


