package tfg.sal.tripl.appcontent.home.domain

import tfg.sal.tripl.appcontent.home.data.countries.CapitalCity
import tfg.sal.tripl.appcontent.home.data.countries.CapitalCityRepository
import javax.inject.Inject

class CapitalCityUseCase @Inject constructor(private val repository: CapitalCityRepository) {
    suspend operator fun invoke(iso2: String): CapitalCity {
        return repository.getCapitalCity(iso2)
    }
}