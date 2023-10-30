package com.android.example.architecture_basics.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.example.architecture_basics.data.repository.Repository
import com.android.example.architecture_basics.domain.helpers.BeersApiStatus
import com.android.example.architecture_basics.domain.helpers.Util
import com.android.example.architecture_basics.domain.model.BeerDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BeersViewModelTest {

    @RelaxedMockK
    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var util: Util

    private lateinit var beersViewModel: BeersViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        beersViewModel = BeersViewModel(repository, util)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando se obtiene el listado de beers del repositorio setearlo en livedata`() = runTest {
        //Given
        val beerList = listOf(
            BeerDomain(
                1,
                "Buzz",
                "A Real Bitter Experience.",
                "09/2007",
                "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
                "https://images.punkapi.com/v2/keg.png",
                4.5,
                60.0
            ),
            BeerDomain(
                2,
                "Trashy Blonde",
                "You Know You Shouldn't,",
                "04/2008",
                "A titillating, neurotic, peroxide punk of a Pale Ale. Combining attitude, style, substance, and a little bit of low self esteem for good measure; what would your mother say? The seductive lure of the sassy passion fruit hop proves too much to resist. All that is even before we get onto the fact that there are no additives, preservatives, pasteurization or strings attached. All wrapped up with the customary BrewDog bite and imaginative twist.",
                "https://images.punkapi.com/v2/2.png",
                4.1,
                41.5
            )
        )
        val status = BeersApiStatus.DONE
        coEvery { repository.fetchBeers(false) } returns beerList

        //When
        beersViewModel.getBeers()

        //Then
        assert(beersViewModel.beers.value == beerList)
        assert(beersViewModel.status.value == status)
        coVerify(exactly = 0) { util.guardaLog("") }
    }

    @Test
    fun `cuando falla fetchBeers setear listado vacio y estado error en livedata`() = runTest {
        //Given
        val beerList = emptyList<BeerDomain>()
        val status = BeersApiStatus.ERROR
        val exception = Exception()
        coEvery { repository.fetchBeers(false) } throws exception

        //When
        beersViewModel.getBeers()

        //Then
        assert(beersViewModel.beers.value == beerList)
        assert(beersViewModel.status.value == status)
        coVerify(exactly = 1) { util.guardaLog("BeersViewModel getBeers(): ${exception.message}") }
    }

    @Test
    fun `cuando updateCurrentBeer setear beer pasada por parametro en livedata currentBeer`() = runTest {
        //Given
        val currentBeer = BeerDomain(
            1,
            "Buzz",
            "A Real Bitter Experience.",
            "09/2007",
            "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            "https://images.punkapi.com/v2/keg.png",
            4.5,
            60.0
        )

        //When
        beersViewModel.updateCurrentBeer(currentBeer)

        //Then
        assert(beersViewModel.currentBeer.value == currentBeer)
    }




}