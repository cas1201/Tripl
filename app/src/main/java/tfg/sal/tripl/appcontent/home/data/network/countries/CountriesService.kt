package tfg.sal.tripl.appcontent.home.data.network.countries

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tfg.sal.tripl.appcontent.home.data.countries.Countries
import javax.inject.Inject

class CountriesService @Inject constructor(private val countriesClient: CountriesClient) {
    private val url = "https://countriesnow.space/api/v0.1/countries"

    suspend fun getCountriesList(): Countries {
        return withContext(Dispatchers.IO) {
            val response = countriesClient.getCountriesList(url)
            Countries(response.body())
        }
    }
}