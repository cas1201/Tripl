package tfg.sal.tripl.appcontent.trip.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary
import tfg.sal.tripl.core.Routes

class TripViewModel : ViewModel() {

    private val _savedItineraries = MutableLiveData<List<SavedItinerary>>()
    val savedItineraries: LiveData<List<SavedItinerary>> = _savedItineraries

    fun saveItinerary(
        filteredPois: List<POIResponse>?,
        poiMarkerCoordinates: List<LatLng>?,
        cameraPositionState: CameraPositionState?,
        countryName: String?,
        countryCity: String?
    ) {
        val sisl = mutableListOf<SavedItinerary>()
        val si = SavedItinerary(
            filteredPois,
            poiMarkerCoordinates,
            cameraPositionState,
            countryName,
            countryCity
        )
        sisl.add(si)
        savedItineraries.value?.forEach { sisl.add(it) }
        _savedItineraries.value = sisl
    }

    fun firestoreSaveItinerary() {

    }

    fun firestoreGetItinerary() {

    }

    fun onSavedItineraryCardClick(itineraryViewModel: ItineraryViewModel) {
        Log.i("itinerarysave", "itinerary card clicked")
    }

    fun onIndexChange(bottomIndex: Int, navigationController: NavHostController) {
        when (bottomIndex) {
            1 -> navigationController.navigate(Routes.HomeScreen.route)
            2 -> navigationController.navigate(Routes.TripScreen.route)
            3 -> navigationController.navigate(Routes.ProfileScreen.route)
        }
    }
}