package tfg.sal.tripl.appcontent.home.data.countries

import com.google.gson.annotations.SerializedName

data class CountriesData(
    @SerializedName("iso2") val countryIso: String,
    @SerializedName("flag") var countryFlag: String,
    @SerializedName("name") val countryName: String,
    @SerializedName("capital") val capitalCity: String,
    @SerializedName("cities") var countryCities: List<String>?
)
