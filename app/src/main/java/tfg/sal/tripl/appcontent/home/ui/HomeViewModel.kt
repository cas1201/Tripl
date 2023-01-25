package tfg.sal.tripl.appcontent.home.ui

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.appcontent.home.data.countries.Countries
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.data.poi.PointsOfInterest
import tfg.sal.tripl.appcontent.home.domain.CountriesUseCase
import tfg.sal.tripl.appcontent.home.domain.POIUseCase
import tfg.sal.tripl.core.Routes
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.sqrt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countriesUseCase: CountriesUseCase,
    private val poiUseCase: POIUseCase,
    @Named("EnEsTranslator") private val enesTranslator: Translator,
    @Named("EsEnTranslator") private val esenTranslator: Translator,
    private val downloadConditions: DownloadConditions
) : ViewModel() {

    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    private val _countries = MutableLiveData<Countries>()
    val countries: LiveData<Countries> = _countries

    private val _countryNames = MutableLiveData<List<String>>()
    val countryNames: LiveData<List<String>> = _countryNames

    private val _countryFlags = MutableLiveData<List<Map<String, String>>>()
    val countryFlags: LiveData<List<Map<String, String>>> = _countryFlags

    private val _suggestedFlags = MutableLiveData<List<Map<String, String>>>()
    val suggestedFlags: LiveData<List<Map<String, String>>> = _suggestedFlags

    private val _expanded = MutableLiveData<Boolean>()
    val expanded: LiveData<Boolean> = _expanded

    private val _interactionSource = MutableLiveData<MutableInteractionSource>()
    val interactionSource: LiveData<MutableInteractionSource> = _interactionSource

    /*private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate*/

    fun setCountriesValue(countries: Countries) {
        _countries.value = countries
    }

    fun setCountryNamesValue(names: List<String>) {
        _countryNames.value = names
    }

    fun setCountryFlagsValue(flags: List<Map<String, String>>) {
        _countryFlags.value = flags
    }

    fun setSuggestedFlags() {
        _suggestedFlags.value = countryFlags.value?.shuffled()?.take(5)
    }

    fun onSelectedTextChange(selectedText: String) {
        _expanded.value = true
        _destination.value = selectedText
    }

    fun onExpandedChange(expanded: Boolean) {
        _expanded.value = expanded
    }

    fun clearTextField() {
        _destination.value = ""
    }

    fun onSearchTrip(navigationController: NavHostController, cardDestination: String? = null) {
        val validateDestination = destinationValidation(_destination.value)

        if (cardDestination != null) {
            _destination.value = cardDestination
            navigationController.navigate(Routes.ItineraryScreen.route) {
                popUpTo(Routes.HomeScreen.route) { inclusive = true }
            }
        } else {
            if (validateDestination) {
                navigationController.navigate(Routes.ItineraryScreen.route) {
                    popUpTo(Routes.HomeScreen.route) { inclusive = true }
                }
            } else {
                Log.i("Validaciones", "NO SE HA SELECCIONADO DESTINO O NO ES VÁLIDO")
            }
        }
    }

    private fun destinationValidation(destination: String?): Boolean {
        return destination != null && destination != "" && countryNames.value?.contains(destination) == true
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
            2 -> navigationController.navigate(Routes.TripScreen.route)
            3 -> navigationController.navigate(Routes.ProfileScreen.route)
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