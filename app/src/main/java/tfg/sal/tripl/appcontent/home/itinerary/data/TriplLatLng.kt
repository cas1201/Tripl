package tfg.sal.tripl.appcontent.home.itinerary.data

data class TriplLatLng(
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this(
        latitude = 0.0,
        longitude = 0.0
    )
}
