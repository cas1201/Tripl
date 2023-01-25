package tfg.sal.tripl.appcontent.home.domain

import tfg.sal.tripl.appcontent.home.data.countries.Countries
import tfg.sal.tripl.appcontent.home.data.countries.CountriesRepository
import javax.inject.Inject

class CountriesUseCase @Inject constructor(private val repository: CountriesRepository) {
    suspend operator fun invoke(): Countries {
        return repository.getCountriesList()
    }
}