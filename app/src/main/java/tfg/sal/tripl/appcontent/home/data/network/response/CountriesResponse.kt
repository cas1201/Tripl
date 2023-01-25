package tfg.sal.tripl.appcontent.home.data.network.response

import com.google.gson.annotations.SerializedName
import tfg.sal.tripl.appcontent.home.data.countries.CountryFlag
import tfg.sal.tripl.appcontent.home.data.countries.CountryName

data class CountriesResponse(
    @SerializedName("name") val name: CountryName,
    @SerializedName("latlng") val coordinates: List<Double>,
    @SerializedName("area") val area: Double,
    @SerializedName("flags") val flags: CountryFlag
)