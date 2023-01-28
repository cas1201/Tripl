package tfg.sal.tripl.appcontent.trip.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.home.ui.BottomNav
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary

@Composable
fun TripScreen(
    viewModel: TripViewModel,
    fireBaseViewModel: FireBaseViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            headerText(
                size = 30,
                text = stringResource(id = R.string.trip),
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        content = {
            tripBody(viewModel = viewModel, itineraryViewModel = itineraryViewModel)
        },
        bottomBar = {
            BottomNav(
                currentDestination
            ) {
                viewModel.onIndexChange(
                    bottomIndex = it,
                    navigationController = navigationController
                )
            }
        }
    )
}

@Composable
fun tripBody(viewModel: TripViewModel, itineraryViewModel: ItineraryViewModel) {
    val savedItineraries: List<SavedItinerary> by viewModel.savedItineraries.observeAsState(initial = listOf())

    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (si in savedItineraries) {
            Card(
                elevation = 10.dp,
                content = { savedItineraryCard(si) },
                modifier = Modifier.clickable {
                    viewModel.onSavedItineraryCardClick(itineraryViewModel)
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun savedItineraryCard(si: SavedItinerary) {
    Row(
        modifier = Modifier.height(150.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
        )
        Column(Modifier.weight(0.7f)) {
            si.countryName?.let { Text(text = it) }
            Spacer(modifier = Modifier.padding(4.dp))
            si.countryCity?.let { Text(text = it) }
        }
    }
}




















