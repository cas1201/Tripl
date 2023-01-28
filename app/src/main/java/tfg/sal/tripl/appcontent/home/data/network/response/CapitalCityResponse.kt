package tfg.sal.tripl.appcontent.home.data.network.response

import com.google.gson.annotations.SerializedName
import tfg.sal.tripl.appcontent.home.data.countries.CapitalCityData

data class CapitalCityResponse(
    @SerializedName("data") val capitalCity: CapitalCityData
)
