package tfg.sal.tripl.appcontent.home.data.network.response

import com.google.gson.annotations.SerializedName

data class CoordinatesResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)
