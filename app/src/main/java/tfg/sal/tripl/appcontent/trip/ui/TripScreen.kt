package tfg.sal.tripl.appcontent.trip.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.home.ui.BottomNav
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TripScreen(
    viewModel: TripViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    viewModel.firestoreGetItinerary()

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
            tripBody(
                modifier = Modifier.padding(16.dp),
                viewModel = viewModel,
                itineraryViewModel = itineraryViewModel,
                navigationController = navigationController
            )
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
fun tripBody(
    modifier: Modifier,
    viewModel: TripViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    val savedItineraries: List<SavedItinerary> by viewModel.savedItineraries.observeAsState(initial = listOf())

    if (savedItineraries.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.no_saved_itineraries),
                textAlign = TextAlign.Center,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    } else {
        Column(modifier.verticalScroll(rememberScrollState())) {
            for (si in savedItineraries) {
                Card(
                    elevation = 10.dp,
                    content = { SavedItineraryCard(si) },
                    modifier = Modifier
                        .height(150.dp)
                        .clickable {
                            viewModel.onSavedItineraryCardClick(
                                si,
                                itineraryViewModel,
                                navigationController
                            )
                        }
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun SavedItineraryCard(si: SavedItinerary) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(si.countryFlag)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = si.countryFlag,
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Column(Modifier.weight(0.7f)) {
            si.countryName?.let { Text(text = it) }
            Spacer(modifier = Modifier.padding(4.dp))
            si.countryCity?.let { Text(text = it) }
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                if (si.poisMarkers?.size?.toString() != null) {
                    Text(text = si.poisMarkers.size.toString() + " ")
                } else {
                    Text(text = "? ")
                }
                Text(text = stringResource(id = R.string.number_pois_saved_itinerary))
            }
        }
    }
}




















