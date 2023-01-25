package tfg.sal.tripl.appcontent.home.data.countries

import tfg.sal.tripl.appcontent.home.data.network.countries.CountriesService
import javax.inject.Inject

class CountriesRepository @Inject constructor(private val api: CountriesService) {
    suspend fun getCountriesList(): Countries {
        return api.getCountriesList()
    }
}