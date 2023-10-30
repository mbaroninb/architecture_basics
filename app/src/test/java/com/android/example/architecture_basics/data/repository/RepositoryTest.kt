package com.android.example.architecture_basics.data.repository

import com.android.example.architecture_basics.data.database.dao.BeerDao
import com.android.example.architecture_basics.data.database.entity.BeerEntity
import com.android.example.architecture_basics.data.network.BeersApiService
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.domain.model.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    /*
    * Parametros pasados por
    * */
    @RelaxedMockK
    private lateinit var beerDao: BeerDao
    @RelaxedMockK
    private lateinit var apiService: BeersApiService

    private lateinit var repository: Repository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        repository = Repository(beerDao,apiService)
    }


    @Test
    fun `cuando obtengo las cervezas y paso isfavorite true las trae de la db`() = runBlocking{
        //Given
        val beerList = listOf(
            BeerEntity(1,
                "Buzz",
                "A Real Bitter Experience.",
                "09/2007",
                "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
                "https://images.punkapi.com/v2/keg.png",
                4.5,
                60.0
            ),
            BeerEntity(2,
                "Trashy Blonde",
                "You Know You Shouldn't,",
                "04/2008",
                "A titillating, neurotic, peroxide punk of a Pale Ale. Combining attitude, style, substance, and a little bit of low self esteem for good measure; what would your mother say? The seductive lure of the sassy passion fruit hop proves too much to resist. All that is even before we get onto the fact that there are no additives, preservatives, pasteurization or strings attached. All wrapped up with the customary BrewDog bite and imaginative twist.",
                "https://images.punkapi.com/v2/2.png",
                4.1,
                41.5)
        )
        coEvery { beerDao.getAllBeers() } returns beerList

        //When
        val response = repository.fetchBeers(true)

        //Then
        coVerify (exactly = 0){ apiService.getBeers()  }
        assert(beerList.map { it.toDomain() } == response)
    }

    @Test
    fun `cuando obtengo las cervezas y paso isfavorite false las trae de la api`() = runBlocking{
        //Given
        val beerList =  listOf(
            BeerApi(1,
                "Buzz",
                "A Real Bitter Experience.",
                "09/2007",
                "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
                "https://images.punkapi.com/v2/keg.png",
                4.5,
                60.0
            ),
            BeerApi(2,
                "Trashy Blonde",
                "You Know You Shouldn't,",
                "04/2008",
                "A titillating, neurotic, peroxide punk of a Pale Ale. Combining attitude, style, substance, and a little bit of low self esteem for good measure; what would your mother say? The seductive lure of the sassy passion fruit hop proves too much to resist. All that is even before we get onto the fact that there are no additives, preservatives, pasteurization or strings attached. All wrapped up with the customary BrewDog bite and imaginative twist.",
                "https://images.punkapi.com/v2/2.png",
                4.1,
                41.5)
        )
        coEvery { apiService.getBeers() } returns beerList

        //When
        val response = repository.fetchBeers(false)

        //Then
        coVerify (exactly = 0){ beerDao.getAllBeers()  }
        assert(beerList.map { it.toDomain() } == response)
    }

}