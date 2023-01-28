package tfg.sal.tripl.appcontent.home.data.countries

import tfg.sal.tripl.appcontent.home.data.network.countries.CoordinatesService
import javax.inject.Inject

class CoordinatesRepository @Inject constructor(private val api: CoordinatesService) {
    suspend fun getCoordinates(city: String, iso2: String): Coordinates {
        return api.getCoordinates(city, iso2)
    }
}