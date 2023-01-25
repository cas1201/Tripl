package tfg.sal.tripl.appcontent.home.data.poi

import tfg.sal.tripl.appcontent.home.data.network.poi.OpenTripMapService
import javax.inject.Inject

class OpenTripMapRepository @Inject constructor(private val api: OpenTripMapService) {
    suspend fun getPOIList(radius: Double, lat: Double, lon: Double): PointsOfInterest {
        return api.getPOIList(radius, lat, lon)
    }
}