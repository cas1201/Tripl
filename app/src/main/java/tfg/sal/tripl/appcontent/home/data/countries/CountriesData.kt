package tfg.sal.tripl.appcontent.home.data.countries

import com.google.gson.annotations.SerializedName

data class CountriesData(
    @SerializedName("iso2") val countryIso: String,
    @SerializedName("country") val countryName: String,
    @SerializedName("cities") val countryCities: List<String>
)
