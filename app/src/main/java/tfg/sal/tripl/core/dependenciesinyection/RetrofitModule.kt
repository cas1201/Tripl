package tfg.sal.tripl.core.dependenciesinyection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tfg.sal.tripl.appcontent.home.data.network.countries.CountriesClient
import tfg.sal.tripl.appcontent.home.data.network.poi.OpenTripMapClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.opentripmap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOpenTripMapClient(retrofit: Retrofit): OpenTripMapClient {
        return retrofit.create(OpenTripMapClient::class.java)
    }

    @Singleton
    @Provides
    fun provideCountriesClient(retrofit: Retrofit): CountriesClient {
        return retrofit.create(CountriesClient::class.java)
    }
}