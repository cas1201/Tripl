package tfg.sal.tripl.appcontent.home.itinerary.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
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
import tfg.sal.tripl.appcontent.home.data.countries.Coordinates
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.data.poi.PointsOfInterest
import tfg.sal.tripl.appcontent.home.domain.POIUseCase
import tfg.sal.tripl.appcontent.home.itinerary.data.POITypes
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.appcontent.trip.ui.TripViewModel
import tfg.sal.tripl.core.Routes
import javax.inject.Inject
import kotlin.math.pow
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

    fun onBackPressed(homeViewModel: HomeViewModel, navigationController: NavHostController) {
        homeViewModel.clearTextField()
        navigationController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.HomeScreen.route) { inclusive = true }
        }
    }

    fun onMenuPressed(expanded: Boolean) {
        _dropDownMenuExpanded.value = expanded
    }

    fun setFilters() {
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
            _poisDistance.value = 25f
        }
    }

    fun onAmountChange(amount: String) {
        val regex = "[0-9]*".toRegex()
        if (amount != "") {
            if (regex.matches(amount)) {
                if (amount.toInt() < 1) {
                    _poisAmount.value = "1"
                } else if (amount.toInt() > 100) {
                    _poisAmount.value = "100"
                } else {
                    _poisAmount.value = amount
                }
            } else {
                _poisAmount.value = regex.find(amount)?.value
            }
        } else {
            _poisAmount.value = ""
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

    fun getPOI(coordinates: Coordinates) {
        val coord = coordinates.coordinates
        viewModelScope.launch {
            if (coord != null && coord.isNotEmpty()) {
                val getPois = poiUseCase(
                    poisDistance.value!! * 1000,
                    coord[0].lat,
                    coord[0].lon
                )
                _pois.value = getPois
            } else {
                Log.i("error", "Coordenadas (itineraryvm linea 177)")
                //error por coordenadas
            }
            if (pois.value != null) {
                filterPOI()
                _poiMarkerCoordinates.value = getPoisMarkerCoordinates(filteredPois.value)
                _filteredPoisCameraPosition.value = calculateMidPoint(filteredPois.value)
                setCameraPosition()
                _showMap.value = true
            } else {
                //error consultando pois
            }
        }
    }


    fun filterPOI() {
        if (poisAmount.value == "") {
            _poisAmount.value = "1"
        }
        val fPois = mutableListOf<POIResponse>()
        pois.value?.pois?.forEach {
            if (it.name != "") {
                if (typeStatus.value?.isEmpty() == true) {
                    if (it.distance <= poisDistance.value!! * 1000) {
                        fPois.add(it)
                    }
                } else {
                    typeStatus.value?.forEach { type ->
                        if (
                            type.value
                            && it.kinds.split(",").contains(type.key)
                            && it.distance <= poisDistance.value!! * 1000
                        ) {
                            fPois.add(it)
                        }
                    }
                }
            }
        }
        if (poisRating.value!! > 0) {
            fPois.removeAll { it.rate != poisRating.value }
        }
        val fPoisShuffled = fPois.shuffled().take(poisAmount.value?.toInt()!!)
        val initialPoi = fPoisShuffled.sortedBy {
            sqrt((it.location.latPoint - 0).pow(2) + (it.location.lonPoint - 0).pow(2))
        }.first()
        var fPoisOrdered = fPoisShuffled.sortedBy {
            sqrt(
                (it.location.latPoint - initialPoi.location.lonPoint).pow(2)
                        + (it.location.lonPoint - initialPoi.location.lonPoint).pow(2)
            )
        }
        _filteredPois.value = fPoisOrdered
        _poiMarkerCoordinates.value = getPoisMarkerCoordinates(filteredPois.value)
    }

    fun searchPoiOnGoogle(context: Context, poiName: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.google.com/search?q=$poiName")
        startActivity(context, openURL, null)
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

    fun onItinerarySave(
        homeViewModel: HomeViewModel,
        tripViewModel: TripViewModel,
        navigationController: NavHostController
    ) {
        tripViewModel.saveItinerary(
            filteredPois.value,
            poiMarkerCoordinates.value,
            cps.value,
            homeViewModel.destinationCountry.value,
            homeViewModel.destinationCity.value
        )
        homeViewModel.clearTextField()
        navigationController.navigate(Routes.TripScreen.route) {
            popUpTo(Routes.TripScreen.route) { inclusive = true }
        }
    }
}