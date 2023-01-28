package tfg.sal.tripl.appcontent.home.ui

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.appcontent.home.data.countries.CountriesData
import tfg.sal.tripl.appcontent.home.domain.CoordinatesUseCase
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.core.Routes
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coordinatesUseCase: CoordinatesUseCase,
    @Named("EnEsTranslator") private val enesTranslator: Translator,
    @Named("EsEnTranslator") private val esenTranslator: Translator,
    private val downloadConditions: DownloadConditions
) : ViewModel() {

    private val _destinationCountry = MutableLiveData<String>()
    val destinationCountry: LiveData<String> = _destinationCountry

    private val _destinationCountryIso = MutableLiveData<String>()
    val destinationCountryIso: LiveData<String> = _destinationCountryIso

    private val _destinationCity = MutableLiveData<String>()
    val destinationCity: LiveData<String> = _destinationCity

    private val _countries = MutableLiveData<List<CountriesData>>()
    val countries: LiveData<List<CountriesData>> = _countries

    private val _expandedCountries = MutableLiveData<Boolean>()
    val expandedCountries: LiveData<Boolean> = _expandedCountries

    private val _expandedCities = MutableLiveData<Boolean>()
    val expandedCities: LiveData<Boolean> = _expandedCities

    private val _interactionSource = MutableLiveData<MutableInteractionSource>()
    val interactionSource: LiveData<MutableInteractionSource> = _interactionSource

    private val _countryFlags = MutableLiveData<List<Map<String, String>>>()
    val countryFlags: LiveData<List<Map<String, String>>> = _countryFlags

    private val _suggestedFlags = MutableLiveData<List<Map<String, String>>>()
    val suggestedFlags: LiveData<List<Map<String, String>>> = _suggestedFlags

    fun sanitizeFlagMap(input: Any): String {
        return input.toString().replace("[", "").replace("]", "")
    }

    suspend fun setCountriesValue(
        countries: MutableList<CountriesData>?,
        flagsAndCities: List<CountriesData>?
    ) {
        countries?.forEach { country ->
            flagsAndCities?.forEach { flAndCi ->
                if (country.countryName == flAndCi.countryName) {
                    country.countryFlag =
                        if (!flAndCi.countryFlag.isNullOrBlank()) flAndCi.countryFlag
                        else "https://upload.wikimedia.org/wikipedia/commons/2/2f/Flag_of_the_United_Nations.svg"
                    country.countryCities = flAndCi.countryCities
                }
            }
        }
        setFlags(countries)
        countries?.removeAll { it.countryCities == null }
        _countries.value = countries
    }

    private fun setFlags(countries: List<CountriesData>?) {
        val sfl = mutableListOf<Map<String, String>>()
        countries?.forEach {
            sfl.add(mapOf(it.countryName to it.countryFlag))
        }
        _countryFlags.value = sfl
        _suggestedFlags.value = countryFlags.value?.shuffled()?.take(5)
    }

    fun onSelectedCountryTextChange(selectedText: String) {
        _expandedCountries.value = true
        _destinationCountry.value = selectedText
    }

    fun onSelectedCityTextChange(selectedText: String) {
        _expandedCities.value = true
        _destinationCity.value = selectedText
    }

    fun onExpandedCountriesChange(expanded: Boolean) {
        _expandedCountries.value = expanded
    }

    fun onExpandedCitiesChange(expanded: Boolean) {
        _expandedCities.value = expanded
    }

    fun clearTextField() {
        _destinationCountry.value = ""
        _destinationCity.value = ""
    }

    fun onSearchTrip(
        navigationController: NavHostController,
        itineraryViewModel: ItineraryViewModel,
        cardDestination: String? = null
    ) {
        val validateDestination = destinationValidation()
        viewModelScope.launch {
            if (cardDestination == null) {
                if (validateDestination) {
                    val coordinates = coordinatesUseCase(
                        destinationCity.value!!,
                        destinationCountryIso.value!!
                    )
                    itineraryViewModel.getPOI(coordinates)
                    navigationController.navigate(Routes.ItineraryScreen.route) {
                        popUpTo(Routes.HomeScreen.route) { inclusive = true }
                    }
                } else {
                    Log.i("Validaciones", "NO SE HA SELECCIONADO DESTINO O NO ES VÁLIDO")
                }
            } else {
                _destinationCountry.value = cardDestination
                countries.value?.forEach {
                    if (it.countryName == destinationCountry.value) {
                        _destinationCity.value = it.capitalCity
                        _destinationCountryIso.value = it.countryIso
                    }
                }
                val coordinates = coordinatesUseCase(
                    destinationCity.value!!,
                    destinationCountryIso.value!!
                )
                itineraryViewModel.getPOI(coordinates)
                navigationController.navigate(Routes.ItineraryScreen.route) {
                    popUpTo(Routes.HomeScreen.route) { inclusive = true }
                }
            }
        }
    }

    private fun destinationValidation(): Boolean {
        var country = false
        var city = false
        var valid = false

        if (destinationCountry.value?.isBlank() == false && destinationCity.value?.isBlank() == false) {
            countries.value?.forEach {
                if (it.countryName == destinationCountry.value) {
                    _destinationCountryIso.value = it.countryIso
                    country = true
                }
                if (it.countryCities!!.contains(destinationCity.value)) {
                    city = true
                }
            }
        }
        if (country && city) {
            valid = true
        }
        return valid
    }

    fun translateToSpanish(text: String): String {
        var translatedText = ""
        enesTranslator.downloadModelIfNeeded(downloadConditions)
            .addOnCompleteListener {
                enesTranslator.translate("text")
                    .addOnCompleteListener { translation ->
                        translatedText = translation.result
                        Log.i("traductor", translation.result)
                    }
                    .addOnFailureListener { failure ->
                        Log.i("traductor", "fallo traduccion: $failure")
                    }
            }
            .addOnFailureListener { failure ->
                Log.i("traductor", "fallo descarga: $failure")
            }
        return translatedText
    }

    fun onIndexChange(selectedIndex: Int, navigationController: NavHostController) {
        when (selectedIndex) {
            1 -> navigationController.navigate(Routes.HomeScreen.route)
            2 -> {
                navigationController.navigate(Routes.TripScreen.route)
                clearTextField()
            }
            3 -> {
                navigationController.navigate(Routes.ProfileScreen.route)
                clearTextField()
            }
        }
    }
/*
fun onStartDateChange(startDate: String) {
    if (startDate != "") {
        _startDate.value = startDate
    }
}

fun onEndDateChange(endDate: String) {
    if (endDate != "") {
        _endDate.value = endDate
    }
}

private fun dateValidation(startDate: String?, endDate: String? = null): Boolean {
    val dateCompare = endDate?.let { startDate?.compareTo(it) }
    return if (dateCompare != null) {
        when {
            dateCompare <= 0 -> {
                true
            }
            else -> {
                false
            }
        }
    } else {
        false
    }
}*/
}