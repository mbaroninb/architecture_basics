package com.android.example.architecture_basics.domain.helpers

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnLayout
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class BeersListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout
) : OnBackPressedCallback(slidingPaneLayout.isOpen && slidingPaneLayout.isSlideable),
    SlidingPaneLayout.PanelSlideListener {

    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }

    override fun handleOnBackPressed() {
        if (slidingPaneLayout.isSlideable) slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View) {
        isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
    }

    fun onResumedCallback(){
        /*
        * Durante las rotaciones, isSlideable() no es correcto hasta que no se haya diseñada la
        * view, por lo que restablecemos el comportamiento del boton "Back" solo despues de pasar el
        * diseño.
        * */
        slidingPaneLayout.requestLayout()
        slidingPaneLayout.doOnLayout {
            isEnabled = slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen
        }
    }

    fun onPausedCallback(){
        // El interceptor del botón "Back" se desactiva al cambiar las pestañas en el bottomNavBar.
        isEnabled = false
    }
}