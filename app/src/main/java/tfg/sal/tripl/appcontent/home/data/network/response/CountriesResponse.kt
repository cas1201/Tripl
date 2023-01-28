package tfg.sal.tripl.appcontent.home.data.network.response

import com.google.gson.annotations.SerializedName
import tfg.sal.tripl.appcontent.home.data.countries.CountriesData

data class CountriesResponse(
    @SerializedName("data") val countriesData: List<CountriesData>
)