package tfg.sal.tripl.appcontent.home.data.network.countries

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import tfg.sal.tripl.appcontent.home.data.network.response.CountriesResponse

interface CountriesClient {
    @GET
    suspend fun getCountriesList(@Url url: String): Response<CountriesResponse>
}