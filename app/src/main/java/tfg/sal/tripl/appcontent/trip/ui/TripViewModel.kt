package tfg.sal.tripl.appcontent.trip.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.domain.CoordinatesUseCase
import tfg.sal.tripl.appcontent.home.itinerary.data.TriplLatLng
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val coordinatesUseCase: CoordinatesUseCase,
    private val firestore: FirebaseFirestore
) :
    ViewModel() {

    private val savedItinerariesCollectionName = "savedItineraries"

    private val _savedItineraries = MutableLiveData<List<SavedItinerary>>()
    val savedItineraries: LiveData<List<SavedItinerary>> = _savedItineraries

    fun saveItinerary(
        filteredPois: List<POIResponse>?,
        poiMarkerCoordinates: List<TriplLatLng>?,
        cameraPosition: List<Double>?,
        countryName: String?,
        countryCity: String?,
        countryFlag: String
    ) {
        val sisl = mutableListOf<SavedItinerary>()
        val si = SavedItinerary(
            filteredPois,
            poiMarkerCoordinates,
            cameraPosition,
            countryName,
            countryCity,
            countryFlag
        )
        sisl.add(si)
        savedItineraries.value?.forEach { savedItinerary ->
            if (si.pois?.sortedBy { it.name } == savedItinerary.pois?.sortedBy { it.name }) {
                Log.i(
                    "saveitinerary",
                    "Error al añadir itinerario porque es igual que uno que ya existe"
                )
                // error al añadir itinerario porque es igual que uno que ya existe
            } else {
                sisl.add(savedItinerary)
            }
        }
        _savedItineraries.value = sisl
        firestoreSaveItinerary(si)
    }

    fun firestoreSaveItinerary(si: SavedItinerary) {
        firestore.collection(savedItinerariesCollectionName).document(si.countryName!!)
            .set(si)
    }

    fun firestoreGetItinerary() {
        if (savedItineraries.value == null || savedItineraries.value?.isEmpty() == true) {
            firestore.collection(savedItinerariesCollectionName)
                .get()
                .addOnSuccessListener {
                    _savedItineraries.value = it.toObjects(SavedItinerary::class.java)
                }
        }
    }

    fun deleteItinerary(itinerary: SavedItinerary) {

    }

    fun onSavedItineraryCardClick(
        savedItinerary: SavedItinerary,
        itineraryViewModel: ItineraryViewModel,
        navigationController: NavHostController
    ) {
        viewModelScope.launch {
            val coordinates = coordinatesUseCase(
                savedItinerary.countryCity!!,
                savedItinerary.countryName!!
            )
            itineraryViewModel.getPOI(
                coordinates,
                savedItinerary.pois,
                savedItinerary.poisMarkers,
                savedItinerary.cameraPosition
            )
            navigationController.navigate(Routes.ItineraryScreen.route) {
                popUpTo(Routes.TripScreen.route) { inclusive = true }
            }
        }
    }

    fun onIndexChange(bottomIndex: Int, navigationController: NavHostController) {
        when (bottomIndex) {
            1 -> navigationController.navigate(Routes.HomeScreen.route)
            2 -> navigationController.navigate(Routes.TripScreen.route)
            3 -> navigationController.navigate(Routes.ProfileScreen.route)
        }
    }
}