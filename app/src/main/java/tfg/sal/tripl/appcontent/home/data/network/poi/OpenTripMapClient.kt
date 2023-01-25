package tfg.sal.tripl.appcontent.home.data.network.poi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse

interface OpenTripMapClient {
    @GET("/0.1/en/places/radius")
    suspend fun getPOIList(
        @Query("format") format: String,
        @Query("lang") lang: String,
        @Query("radius") radius: Double,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("apikey") apikey: String
    ): Response<List<POIResponse>>
}