package tfg.sal.tripl.appcontent.trip.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.trip.data.SavedItinerary

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TripScreen(
    viewModel: TripViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    viewModel.firestoreGetItinerary(LocalContext.current)

    val showAlertDialog: Boolean by viewModel.showAlertDialog.observeAsState(initial = false)
    val cardSavedItinerary: SavedItinerary by viewModel.cardSavedItinerary.observeAsState(initial = SavedItinerary())

    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    ModalDrawer(
        drawerContent = {
            if (showAlertDialog) {
                DeleteItineraryAlertDialog(
                    si = cardSavedItinerary,
                    showAlertDialog = showAlertDialog,
                    viewModel = viewModel
                )
            }
        },
        content = {
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
                    TripBody(
                        modifier = Modifier.padding(16.dp),
                        showAlertDialog = showAlertDialog,
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
    )
}

@Composable
fun TripBody(
    modifier: Modifier,
    showAlertDialog: Boolean,
    viewModel: TripViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    val context = LocalContext.current
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
                    content = { SavedItineraryCard(si, showAlertDialog, viewModel) },
                    modifier = Modifier
                        .height(150.dp)
                        .clickable {
                            viewModel.onSavedItineraryCardClick(
                                context,
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
fun SavedItineraryCard(si: SavedItinerary, showAlertDialog: Boolean, viewModel: TripViewModel) {
    Box {
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
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.delete_itinerary),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .clickable {
                    viewModel.cardSavedItinerary(si)
                    viewModel.showAlertDialog(!showAlertDialog)
                }
        )
    }
}

@Composable
fun DeleteItineraryAlertDialog(si: SavedItinerary, showAlertDialog: Boolean, viewModel: TripViewModel) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { viewModel.showAlertDialog(!showAlertDialog) },
        title = {
            Text(text = stringResource(id = R.string.delete_itinerary_dialog))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_itinerary_dialog_text))
        },
        buttons = {
            Row(Modifier.padding(25.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                TriplButton(
                    text = stringResource(id = R.string.delete_itinerary_dialog_yes),
                    buttonEnable = true,
                    alertDialog = true
                ) {
                    viewModel.deleteItinerary(context, si)
                    viewModel.showAlertDialog(!showAlertDialog)
                }
                Spacer(modifier = Modifier.weight(1f))
                TriplButton(
                    text = stringResource(id = R.string.delete_itinerary_dialog_no),
                    buttonEnable = true,
                    alertDialog = true
                ) { viewModel.showAlertDialog(!showAlertDialog) }
            }
        }
    )
}




















