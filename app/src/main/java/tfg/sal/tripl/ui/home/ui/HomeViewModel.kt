package tfg.sal.tripl.ui.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.model.Routes
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _selectedText = MutableLiveData<String>()
    val selectedText: LiveData<String> = _selectedText

    private val _expanded = MutableLiveData<Boolean>()
    val expanded: LiveData<Boolean> = _expanded

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    fun onSelectedTextChange(selectedText: String) {
        _selectedText.value = selectedText
    }

    fun onExpandedChange(expanded: Boolean) {
        _expanded.value = expanded
    }

    fun onStartDateChange(expanded: String) {
        _startDate.value = expanded
    }

    fun onEndDateChange(expanded: String) {
        _endDate.value = expanded
    }

    fun onIndexChange(selectedIndex: Int, navigationController: NavHostController) {
        when (selectedIndex) {
            1 -> {
                navigationController.navigate(Routes.HomeScreen.route)
            }
            2 -> {
                navigationController.navigate(Routes.TripScreen.route)
            }
            3 -> {
                navigationController.navigate(Routes.ProfileScreen.route)
            }
        }
    }
}