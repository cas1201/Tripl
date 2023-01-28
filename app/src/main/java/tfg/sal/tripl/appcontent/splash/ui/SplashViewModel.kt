package tfg.sal.tripl.appcontent.splash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tfg.sal.tripl.appcontent.home.domain.CountriesUseCase
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val countriesUseCase: CountriesUseCase) :
    ViewModel() {

    private val _continueToHome = MutableLiveData<Boolean>()
    val continueToHome: LiveData<Boolean> = _continueToHome

    fun initializeUser(
        user: FirebaseUser?,
        goHome: Boolean,
        navigationController: NavHostController
    ) {
        if (goHome) {
            if (user != null) {
                navigationController.navigate(Routes.HomeScreen.route) {
                    popUpTo(Routes.HomeScreen.route) { inclusive = true }
                }
                _continueToHome.value = false
            } else {
                navigationController.navigate(Routes.LoginScreen.route) {
                    popUpTo(Routes.LoginScreen.route) { inclusive = true }
                }
            }
        }
    }

    fun getCountries(homeViewModel: HomeViewModel) {
        val countries = homeViewModel.countries.value
        if (countries?.isEmpty() == true || countries == null) {
            //val names = mutableListOf<String>()
            //val flags = mutableListOf<Map<String, String>>()
            viewModelScope.launch {
                val countriesObject = countriesUseCase().countries?.countriesData
                val flagsAndCitiesObject = countriesUseCase(true).countries?.countriesData
                //countriesObject.countries?.countriesData?.forEach { names.add(it.countryName) }
                //countriesObject.countries?.countriesData?.forEach { flags.add(mapOf(it.countryName to it.flags.png)) }
                homeViewModel.setCountriesValue(countriesObject, flagsAndCitiesObject)
                _continueToHome.value = true
            }
        }
    }
}