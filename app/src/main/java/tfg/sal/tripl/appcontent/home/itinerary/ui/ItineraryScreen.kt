package tfg.sal.tripl.appcontent.home.itinerary.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.data.network.response.POIResponse
import tfg.sal.tripl.appcontent.home.itinerary.data.POITypes
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.TriplTextField
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.Divisor
import tfg.sal.tripl.appcontent.trip.ui.TripViewModel
import tfg.sal.tripl.theme.MapsTrackColor
import tfg.sal.tripl.theme.SecondaryColor

@Composable
fun ItineraryScreen(
    viewModel: ItineraryViewModel,
    homeViewModel: HomeViewModel,
    tripViewModel: TripViewModel,
    navigationController: NavHostController
) {
    val poisAmount: String by viewModel.poisAmount.observeAsState(initial = "")
    val poisRating: Int by viewModel.poisRating.observeAsState(initial = -1)
    val poisDistance: Float by viewModel.poisDistance.observeAsState(initial = 25f)
    val showMap: Boolean by viewModel.showMap.observeAsState(initial = false)

    BackHandler {
        viewModel.onBackPressed(homeViewModel, navigationController)
    }

    val scaffoldState = rememberScaffoldState()
    if (showMap) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                ItineraryHeader(
                    viewModel = viewModel,
                    homeViewModel = homeViewModel,
                    poisAmount = poisAmount,
                    poisRating = poisRating,
                    poisDistance = poisDistance,
                    navigationController = navigationController
                )
            },
            content = {
                ItineraryMap(viewModel = viewModel)
            },
            bottomBar = {
                ItinerarySave {
                    viewModel.onItinerarySave(
                        homeViewModel,
                        tripViewModel,
                        navigationController
                    )
                }
            }
        )
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = SecondaryColor)
        }
    }
}

@Composable
fun ItineraryHeader(
    viewModel: ItineraryViewModel,
    homeViewModel: HomeViewModel,
    poisAmount: String,
    poisRating: Int,
    poisDistance: Float,
    navigationController: NavHostController
) {

    val dropDownMenuExpanded: Boolean by viewModel.dropDownMenuExpanded.observeAsState(initial = false)
    val filters = listOf(R.string.amount, R.string.rating, R.string.type, R.string.distance)

    Column(horizontalAlignment = Alignment.End) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(TextFieldDefaults.MinHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { viewModel.onBackPressed(homeViewModel, navigationController) }
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.menu),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { viewModel.onMenuPressed(!dropDownMenuExpanded) }
            )
        }
        Box {
            DropdownMenu(
                modifier = Modifier.scrollable(
                    rememberScrollState(),
                    orientation = Orientation.Vertical
                ),
                expanded = dropDownMenuExpanded,
                onDismissRequest = { viewModel.onMenuPressed(false) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { viewModel.onMenuPressed(!dropDownMenuExpanded) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.hide_menu))
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = stringResource(id = R.string.hide_menu)
                    )
                }
                Spacer(Modifier.padding(top = 8.dp))
                Divisor()
                filters.forEach { filter ->
                    Column(
                        Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = stringResource(filter))
                        Spacer(modifier = Modifier.padding(8.dp))
                        when (filter) {
                            R.string.amount -> {
                                TriplTextField(
                                    label = stringResource(id = R.string.amount_label),
                                    type = KeyboardType.Number,
                                    value = poisAmount,
                                    modifier = Modifier,
                                ) { viewModel.onAmountChange(it) }
                            }
                            R.string.rating -> {
                                Column(Modifier.fillMaxWidth()) {
                                    for (r in listOf(1, 2, 3)) {
                                        ratingRadioButton(
                                            ratingLvl = r,
                                            poisRating = poisRating,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                            R.string.type -> {
                                typeCheckBox(POITypes.values().toList(), viewModel)
                            }
                            R.string.distance -> {
                                Box {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Slider(
                                            modifier = Modifier
                                                .width(200.dp)
                                                .height(10.dp),
                                            value = poisDistance,
                                            valueRange = 1f..10000f,
                                            onValueChange = {
                                                viewModel.onDistanceChange(it)
                                            }
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .weight(1f)
                                        )
                                        Text(text = poisDistance.toInt().toString() + " Km")
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Divisor()
                    }
                }
                Box(Modifier.padding(16.dp)) {
                    TriplButton(text = stringResource(R.string.save_filters), buttonEnable = true) {
                        viewModel.filterPOI()
                        viewModel.onMenuPressed(false)
                    }
                }
            }
        }
    }
}

@Composable
fun ratingRadioButton(ratingLvl: Int, poisRating: Int, viewModel: ItineraryViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = poisRating == ratingLvl,
            onClick = { viewModel.onRatingChange(ratingLvl) }
        )
        for (s in 0 until ratingLvl) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(id = R.string.ratingStar)
            )
        }
    }
}

@Composable
fun typeCheckBox(types: List<POITypes>, viewModel: ItineraryViewModel) {
    val arqStatus: Boolean by viewModel.arqStatus.observeAsState(initial = false)
    val cultStatus: Boolean by viewModel.cultStatus.observeAsState(initial = false)
    val industStatus: Boolean by viewModel.industStatus.observeAsState(initial = false)
    val natStatus: Boolean by viewModel.natStatus.observeAsState(initial = false)
    val relStatus: Boolean by viewModel.relStatus.observeAsState(initial = false)
    val otherStatus: Boolean by viewModel.otherStatus.observeAsState(initial = false)
    val status = mapOf(
        0 to arqStatus,
        1 to cultStatus,
        2 to industStatus,
        3 to natStatus,
        4 to relStatus,
        5 to otherStatus
    )
    Column {
        types.forEach { poitypes ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = status[poitypes.ordinal]!!,
                    onCheckedChange = {
                        viewModel.onTypeChange(
                            poitypes.ordinal,
                            !status[poitypes.ordinal]!!
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = poitypes.name)
            }
        }
    }
}

@Composable
fun ItineraryMap(
    viewModel: ItineraryViewModel
) {
    val poiMarkerCoordinates: List<LatLng> by viewModel.poiMarkerCoordinates.observeAsState(initial = listOf())
    val filteredPois: List<POIResponse> by viewModel.filteredPois.observeAsState(initial = listOf())
    val cameraPositionState: CameraPositionState by viewModel.cps.observeAsState(initial = CameraPositionState())
    val context = LocalContext.current

    GoogleMap(cameraPositionState = cameraPositionState) {
        if (filteredPois.isNotEmpty()) {
            for (p in filteredPois) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            p.location.latPoint,
                            p.location.lonPoint
                        )
                    ),
                    title = p.name,
                    onInfoWindowClick = { viewModel.searchPoiOnGoogle(context, p.name) }
                )
            }
        }
        Polyline(
            points = poiMarkerCoordinates,
            geodesic = true,
            color = MapsTrackColor,
            visible = true
        )
    }
}

@Composable
fun ItinerarySave(onItinerarySave: () -> Unit) {
    Box(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(TextFieldDefaults.MinHeight)
    ) {
        TriplButton(
            text = stringResource(id = R.string.save_itinerary),
            buttonEnable = true
        ) { onItinerarySave() }
    }
}

