package tfg.sal.tripl.appcontent.home.domain

import tfg.sal.tripl.appcontent.home.data.countries.Coordinates
import tfg.sal.tripl.appcontent.home.data.countries.CoordinatesRepository
import javax.inject.Inject

class CoordinatesUseCase @Inject constructor(private val repository: CoordinatesRepository) {
    suspend operator fun invoke(city: String, iso2: String): Coordinates {
        return repository.getCoordinates(city, iso2)
    }
}