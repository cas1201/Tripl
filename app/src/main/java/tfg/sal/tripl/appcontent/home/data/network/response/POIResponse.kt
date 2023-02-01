package tfg.sal.tripl.appcontent.home.data.network.response

import com.google.gson.annotations.SerializedName
import tfg.sal.tripl.appcontent.home.data.poi.POICoordinates

data class POIResponse(
    @SerializedName("name") val name: String,
    @SerializedName("dist") val distance: Double,
    @SerializedName("rate") val rate: Int,
    @SerializedName("kinds") val kinds: String,
    @SerializedName("point") val location: POICoordinates
) {
    constructor() : this(
        name = "",
        distance = 0.0,
        rate = 0,
        kinds = "",
        location = POICoordinates()
    )
}
