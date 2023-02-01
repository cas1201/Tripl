package tfg.sal.tripl.appcontent.home.data.poi

import com.google.gson.annotations.SerializedName

data class POICoordinates(
    @SerializedName("lat") val latPoint: Double,
    @SerializedName("lon") val lonPoint: Double,
) {
    constructor() : this(
        latPoint = 0.0,
        lonPoint = 0.0
    )
}
