package tfg.sal.tripl.appcontent.trip.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.itinerary.data.TriplLatLng

data class SavedItinerary(
    val pois: List<POIResponse>?,
    val poisMarkers: List<TriplLatLng>?,
    val cameraPosition: List<Double>?,
    val countryName: String?,
    val countryCity: String?,
    val countryFlag: String
) {
    constructor() : this(
        pois = null,
        poisMarkers = null,
        cameraPosition = null,
        countryName = null,
        countryCity = null,
        countryFlag = ""
    )
}
