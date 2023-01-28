package tfg.sal.tripl.appcontent.home.data.countries

import tfg.sal.tripl.appcontent.home.data.network.countries.CapitalCityService
import javax.inject.Inject

class CapitalCityRepository @Inject constructor(private val api: CapitalCityService) {
    suspend fun getCapitalCity(iso2: String): CapitalCity {
        return api.getCapitalCity(iso2)
    }
}