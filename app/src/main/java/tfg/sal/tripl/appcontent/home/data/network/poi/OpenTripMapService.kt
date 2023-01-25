package tfg.sal.tripl.appcontent.home.data.network.poi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tfg.sal.tripl.appcontent.home.data.poi.PointsOfInterest
import javax.inject.Inject

class OpenTripMapService @Inject constructor(private val openTripMapClient: OpenTripMapClient) {
    private val apikey = "5ae2e3f221c38a28845f05b67f5f73af50658a590d207167988960af"
    private val format = "json"
    private val lang = "en"

    suspend fun getPOIList(radius: Double, lat: Double, lon: Double): PointsOfInterest {
        return withContext(Dispatchers.IO) {
            val response = openTripMapClient.getPOIList(format, lang, radius, lat, lon, apikey)
            PointsOfInterest(response.body())
        }
    }
}