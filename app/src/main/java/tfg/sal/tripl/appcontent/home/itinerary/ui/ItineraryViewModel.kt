package tfg.sal.tripl.appcontent.home.itinerary.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.data.countries.Countries
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.data.poi.PointsOfInterest
import tfg.sal.tripl.appcontent.home.domain.POIUseCase
import tfg.sal.tripl.appcontent.home.itinerary.data.POITypes
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.core.Routes
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class ItineraryViewModel @Inject constructor(private val poiUseCase: POIUseCase) : ViewModel() {
    private val types = mutableMapOf<String, Boolean>()

    private val _dropDownMenuExpanded = MutableLiveData<Boolean>()
    val dropDownMenuExpanded: LiveData<Boolean> = _dropDownMenuExpanded

    private val _showMap = MutableLiveData<Boolean>()
    val showMap: LiveData<Boolean> = _showMap

    private val _poisAmount = MutableLiveData<String>()
    val poisAmount: LiveData<String> = _poisAmount

    private val _poisRating = MutableLiveData<Int>()
    val poisRating: LiveData<Int> = _poisRating

    private val _typeStatus = MutableLiveData<Map<String, Boolean>>()
    val typeStatus: LiveData<Map<String, Boolean>> = _typeStatus

    private val _arqStatus = MutableLiveData<Boolean>()
    val arqStatus: LiveData<Boolean> = _arqStatus

    private val _cultStatus = MutableLiveData<Boolean>()
    val cultStatus: LiveData<Boolean> = _cultStatus

    private val _industStatus = MutableLiveData<Boolean>()
    val industStatus: LiveData<Boolean> = _industStatus

    private val _natStatus = MutableLiveData<Boolean>()
    val natStatus: LiveData<Boolean> = _natStatus

    private val _relStatus = MutableLiveData<Boolean>()
    val relStatus: LiveData<Boolean> = _relStatus

    private val _otherStatus = MutableLiveData<Boolean>()
    val otherStatus: LiveData<Boolean> = _otherStatus

    private val _poisDistance = MutableLiveData<Float>()
    val poisDistance: LiveData<Float> = _poisDistance

    private val _pois = MutableLiveData<PointsOfInterest>()
    val pois: LiveData<PointsOfInterest> = _pois

    private val _filteredPois = MutableLiveData<List<POIResponse>>()
    val filteredPois: LiveData<List<POIResponse>> = _filteredPois

    private val _poiMarkerCoordinates = MutableLiveData<List<LatLng>>()
    val poiMarkerCoordinates: LiveData<List<LatLng>> = _poiMarkerCoordinates

    private val _filteredPoisCameraPosition = MutableLiveData<List<Double>>()
    private val filteredPoisCameraPosition: LiveData<List<Double>> = _filteredPoisCameraPosition

    private val _cps = MutableLiveData<CameraPositionState>()
    val cps: LiveData<CameraPositionState> = _cps

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.HomeScreen.route) { inclusive = true }
        }
    }

    fun onMenuPressed(expanded: Boolean) {
        _dropDownMenuExpanded.value = expanded
    }

    fun onAmountChange(amount: String) {
        if (amount.toInt() > 50) {
            _poisAmount.value = "50"
        } else {
            _poisAmount.value = amount
        }
    }

    fun onRatingChange(rating: Int) {
        _poisRating.value = rating
    }

    fun onTypeChange(index: Int, status: Boolean) {
        when (index) {
            0 -> {
                _arqStatus.value = status
                types[POITypes.architecture.name] = status
            }
            1 -> {
                _cultStatus.value = status
                types[POITypes.cultural.name] = status
            }
            2 -> {
                _industStatus.value = status
                types[POITypes.industrial_facilities.name] = status
            }
            3 -> {
                _natStatus.value = status
                types[POITypes.natural.name] = status
            }
            4 -> {
                _relStatus.value = status
                types[POITypes.religion.name] = status
            }
            5 -> {
                _otherStatus.value = status
                types[POITypes.other.name] = status
            }
        }
        _typeStatus.value = types
    }

    fun onDistanceChange(distance: Float) {
        _poisDistance.value = distance
        Log.i("distancealert", "${poisDistance.value}")
    }

    fun getPOI(countries: Countries, destination: String) {
        viewModelScope.launch {
            countries.countries?.forEach {
                if (it.name.common == destination) {
                    val countryArea = it.area
                    val countryLat = it.coordinates[0]
                    val countryLon = it.coordinates[1]
                    val getPois = poiUseCase(
                        sqrt((countryArea * 1000000) / Math.PI),
                        countryLat,
                        countryLon
                    )
                    _pois.value = getPois
                }
            }
            if (pois.value != null) {
                _filteredPois.value = pois.value?.pois?.shuffled()?.take(5)
                _poiMarkerCoordinates.value = getPoisMarkerCoordinates(filteredPois.value)
                _filteredPoisCameraPosition.value = calculateMidPoint(filteredPois.value)
                setCameraPosition()
                _showMap.value = true
            } //============================================== Meter mensaje de error al consultar los pois
        }
    }

    fun filterPOI() {
        val fPois = mutableListOf<POIResponse>()
        if (poisAmount.value == null) {
            _poisAmount.value = "5"
        }
        if (poisRating.value == null) {
            _poisRating.value = -1
        }
        if (typeStatus.value == null) {
            _typeStatus.value = mapOf()
        }
        if (poisDistance.value == null) {
            _poisDistance.value = 1f
        }
        pois.value?.pois?.forEach {
            if (typeStatus.value?.isEmpty() == true) {
                if (it.distance / 1000 <= poisDistance.value!!) {
                    fPois.add(it)
                }
            } else {
                typeStatus.value?.forEach { type ->
                    if (
                        type.value
                        && it.kinds.split(",").contains(type.key)
                        && it.distance / 1000 <= poisDistance.value!!
                    ) {
                        fPois.add(it)
                    }
                }
            }
        }
        if (poisRating.value!! > 0) {
            fPois.removeAll { it.rate != poisRating.value }
        }
        _filteredPois.value = fPois.shuffled().take(poisAmount.value?.toInt()!!)
        Log.i("filtros", "${filteredPois.value}")
    }

    private fun getPoisMarkerCoordinates(pois: List<POIResponse>?): List<LatLng> {
        return if (pois != null) {
            val coordsList = mutableListOf<LatLng>()
            pois.forEach {
                coordsList.add(LatLng(it.location.latPoint, it.location.lonPoint))
            }
            coordsList
        } else {
            listOf()
        }
    }

    private fun calculateMidPoint(pois: List<POIResponse>?): List<Double> {
        return if (pois != null) {
            var lat = 0.0
            var lon = 0.0
            pois.forEach {
                lat += it.location.latPoint
                lon += it.location.lonPoint
            }
            listOf(lat.div(pois.size), lon.div(pois.size))
        } else {
            listOf()
        }
    }

    private fun setCameraPosition() {
        val fPC = filteredPoisCameraPosition.value

        if (fPC?.isNotEmpty() == true) {
            val filteredPoisCoords = LatLng(fPC[0], fPC[1])
            val position = CameraPosition.fromLatLngZoom(filteredPoisCoords, 9f)
            _cps.value = CameraPositionState(position)
        }
    }

    fun onItinerarySave(homeViewModel: HomeViewModel, navigationController: NavHostController) {
        navigationController.navigate(Routes.TripScreen.route) {
            popUpTo(Routes.TripScreen.route) { inclusive = true }
        }
        homeViewModel.clearTextField()
    }
}