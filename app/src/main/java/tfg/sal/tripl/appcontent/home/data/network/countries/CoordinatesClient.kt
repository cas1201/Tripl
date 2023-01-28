package tfg.sal.tripl.appcontent.home.data.network.countries

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tfg.sal.tripl.appcontent.home.data.network.response.CoordinatesResponse

interface CoordinatesClient {
    @GET("https://api.openweathermap.org/geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") place: String,
        @Query("appid") apiKey: String
    ): Response<List<CoordinatesResponse>>
}