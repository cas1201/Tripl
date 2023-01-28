package tfg.sal.tripl.appcontent.home.data.network.countries

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import tfg.sal.tripl.appcontent.home.data.network.response.CapitalCityResponse

interface CapitalCityClient {
    @POST("https://countriesnow.space/api/v0.1/countries/capital")
    @Headers("Content-Type: application/json")
    suspend fun getCapitalCity(
        @Body json: String
    ): Response<CapitalCityResponse>
}