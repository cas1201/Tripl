package tfg.sal.tripl.appcontent.home.data.network.countries

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tfg.sal.tripl.appcontent.home.data.countries.Coordinates
import javax.inject.Inject

class CoordinatesService @Inject constructor(private val coordinatesClient: CoordinatesClient) {
    private val apikey = "a450b9b7785773abf8c52a30729919fc"

    suspend fun getCoordinates(city: String, iso2: String): Coordinates {
        return withContext(Dispatchers.IO) {
            val response = coordinatesClient.getCoordinates("$city,$iso2", apikey)
            Coordinates(response.body())
        }
    }
}