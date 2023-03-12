package tfg.sal.tripl.appcontent.trip.data

import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.itinerary.data.TriplLatLng

data class SavedItinerary(
    val siId: String?,
    val pois: List<POIResponse>?,
    val poisMarkers: List<TriplLatLng>?,
    val cameraPosition: List<Double>?,
    val countryName: String?,
    val countryCity: String?,
    val countryFlag: String?
) {
    constructor() : this(
        siId = null,
        pois = null,
        poisMarkers = null,
        cameraPosition = null,
        countryName = null,
        countryCity = null,
        countryFlag = ""
    )
}
