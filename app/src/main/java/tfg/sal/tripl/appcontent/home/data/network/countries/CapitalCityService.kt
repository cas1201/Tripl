package tfg.sal.tripl.appcontent.home.data.network.countries

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import tfg.sal.tripl.appcontent.home.data.countries.CapitalCity
import javax.inject.Inject

class CapitalCityService @Inject constructor(private val capitalCityClient: CapitalCityClient) {

    suspend fun getCapitalCity(iso2: String): CapitalCity {
        val json = JSONObject()
        json.put("iso2", iso2)
        return withContext(Dispatchers.IO) {
            val response = capitalCityClient.getCapitalCity(json.toString())
            Log.i("capitalcity", "$json ${response.raw()} ${response.body()}")
            CapitalCity(response.body())
        }
    }
}