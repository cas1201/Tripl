package tfg.sal.tripl.appcontent.trip.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse

data class SavedItinerary(
    val pois: List<POIResponse>?,
    val poisMarkers: List<LatLng>?,
    val cameraPosition: CameraPositionState?,
    val countryName: String?,
    val countryCity: String?
)
