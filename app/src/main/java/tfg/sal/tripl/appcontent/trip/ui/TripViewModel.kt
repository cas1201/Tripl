package tfg.sal.tripl.appcontent.trip.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.domain.CoordinatesUseCase
import tfg.sal.tripl.appcontent.home.itinerary.data.TriplLatLng
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary
import tfg.sal.tripl.core.Routes
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val coordinatesUseCase: CoordinatesUseCase,
    private val firestore: FirebaseFirestore,
    firebase: FirebaseAuth
) :
    ViewModel() {

    private val userUid = firebase.currentUser!!.uid

    private val _savedItineraries = MutableLiveData<List<SavedItinerary>>()
    val savedItineraries: LiveData<List<SavedItinerary>> = _savedItineraries

    private val _cardSavedItinerary = MutableLiveData<SavedItinerary>()
    val cardSavedItinerary: LiveData<SavedItinerary> = _cardSavedItinerary

    private val _showAlertDialog = MutableLiveData<Boolean>()
    val showAlertDialog: LiveData<Boolean> = _showAlertDialog

    fun showAlertDialog(show: Boolean) {
        _showAlertDialog.value = show
    }

    private fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun refreshClicked(context: Context){
        _savedItineraries.value = emptyList()
        firestoreGetItinerary(context)
    }

    fun cardSavedItinerary(si: SavedItinerary) {
        _cardSavedItinerary.value = si
    }

    fun saveItinerary(
        context: Context,
        filteredPois: List<POIResponse>?,
        poiMarkerCoordinates: List<TriplLatLng>?,
        cameraPosition: List<Double>?,
        countryName: String?,
        countryCity: String?,
        countryFlag: String?
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
        if (!checkIfItineraryExists(si)) {
            firestoreSaveItinerary(context, si)
        } else {
            showToast(context, R.string.itinerary_already_exists)
        }
    }

    private fun firestoreSaveItinerary(context: Context, si: SavedItinerary) {
        firestore.collection(userUid)
            .document("savedItineraries")
            .collection("itineraryData")
            .document("${si.countryName!!}@${si.siId!!}")
            .set(si)
            .addOnFailureListener {
                showToast(context, R.string.save_itinerary_error)
            }
    }

    fun firestoreGetItinerary(context: Context) {
        firestore.collection(userUid)
            .document("savedItineraries")
            .collection("itineraryData")
            .get()
            .addOnSuccessListener {
                _savedItineraries.value = it.toObjects(SavedItinerary::class.java).asReversed()
            }.addOnFailureListener {
                showToast(context, R.string.get_itinerary_error)
            }
    }

    fun deleteItinerary(context: Context, itinerary: SavedItinerary) {
        firestore.collection(userUid)
            .document("savedItineraries")
            .collection("itineraryData")
            .document("${itinerary.countryName!!}@${itinerary.siId!!}")
            .delete()
            .addOnSuccessListener {
                firestoreGetItinerary(context)
            }.addOnFailureListener {
                showToast(context, R.string.delete_itinerary_error)
            }
    }

    private fun checkIfItineraryExists(si: SavedItinerary): Boolean {
        return savedItineraries.value?.any {
            it.countryName == si.countryName &&
                    it.countryCity == si.countryCity &&
                    it.pois?.size == si.pois?.size &&
                    it.pois == si.pois
        } ?: false
    }

    fun onSavedItineraryCardClick(
        context: Context,
        savedItinerary: SavedItinerary,
        itineraryViewModel: ItineraryViewModel,
        navigationController: NavHostController
    ) {
        viewModelScope.launch {
            itineraryViewModel.setDestinationValues(
                savedItinerary.countryName,
                savedItinerary.countryCity,
                savedItinerary.countryFlag
            )
            val coordinates = coordinatesUseCase(
                savedItinerary.countryCity!!,
                savedItinerary.countryName!!
            )
            itineraryViewModel.getPOI(
                context,
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