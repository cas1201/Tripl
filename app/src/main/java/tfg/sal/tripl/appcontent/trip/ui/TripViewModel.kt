package tfg.sal.tripl.appcontent.trip.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val coordinatesUseCase: CoordinatesUseCase,
    private val firestore: FirebaseFirestore,
    private val firebase: FirebaseAuth
) :
    ViewModel() {

    private val savedItinerariesCollectionName = "savedItineraries"

    private val _savedItineraries = MutableLiveData<List<SavedItinerary>>()
    val savedItineraries: LiveData<List<SavedItinerary>> = _savedItineraries

    private val _cardSavedItinerary = MutableLiveData<SavedItinerary>()
    val cardSavedItinerary: LiveData<SavedItinerary> = _cardSavedItinerary

    private val _showAlertDialog = MutableLiveData<Boolean>()
    val showAlertDialog: LiveData<Boolean> = _showAlertDialog

    fun showAlertDialog(show: Boolean) {
        _showAlertDialog.value = show
    }

    fun cardSavedItinerary(si: SavedItinerary) {
        _cardSavedItinerary.value = si
    }

    fun saveItinerary(
        filteredPois: List<POIResponse>?,
        poiMarkerCoordinates: List<TriplLatLng>?,
        cameraPosition: List<Double>?,
        countryName: String?,
        countryCity: String?,
        countryFlag: String
    ) {
        val si = SavedItinerary(
            UUID.randomUUID().toString(),
            filteredPois,
            poiMarkerCoordinates,
            cameraPosition,
            countryName,
            countryCity,
            countryFlag
        )
        firestoreSaveItinerary(si)
    }

    fun firestoreSaveItinerary(si: SavedItinerary) {
        firestore.collection("$savedItinerariesCollectionName@${firebase.currentUser!!.uid}")
            .document(si.countryName!! + "@" + si.siId!!)
            .set(si)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    fun firestoreGetItinerary() {
        firestore.collection("$savedItinerariesCollectionName@${firebase.currentUser!!.uid}")
            .get()
            .addOnSuccessListener {
                _savedItineraries.value = it.toObjects(SavedItinerary::class.java).asReversed()
            }.addOnFailureListener {

            }
    }

    fun deleteItinerary(itinerary: SavedItinerary) {
        firestore.collection("$savedItinerariesCollectionName@${firebase.currentUser!!.uid}")
            .document(itinerary.countryName!! + "@" + itinerary.siId!!)
            .delete()
            .addOnSuccessListener {
                firestoreGetItinerary()
            }.addOnFailureListener {

            }
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