package tfg.sal.tripl.appcontent.home.domain

import tfg.sal.tripl.appcontent.home.data.poi.OpenTripMapRepository
import tfg.sal.tripl.appcontent.home.data.poi.PointsOfInterest
import javax.inject.Inject

class POIUseCase @Inject constructor(private val repository: OpenTripMapRepository) {
    suspend operator fun invoke(radius: Double, lat: Double, lon: Double): PointsOfInterest {
        return repository.getPOIList(radius, lat, lon)
    }
}