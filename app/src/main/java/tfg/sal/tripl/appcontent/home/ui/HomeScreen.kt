package tfg.sal.tripl.appcontent.home.ui

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.data.countries.Countries
import tfg.sal.tripl.appcontent.home.data.countries.CountriesData
import tfg.sal.tripl.appcontent.home.data.network.response.CountriesResponse
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.TriplTextField
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.core.Routes

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    val countriesObject: List<CountriesData> by viewModel.countries.observeAsState(initial = listOf())
    val destinationCountry: String by viewModel.destinationCountry.observeAsState(initial = "")
    val destinationCity: String by viewModel.destinationCity.observeAsState(initial = "")

    val countries = mutableListOf<String>()
    countriesObject.forEach {
        countries.add(it.countryName)
    }

    val cities = mutableListOf<String>()
    countriesObject.forEach {
        if (it.countryName == destinationCountry) {
            it.countryCities?.forEach { city ->
                cities.add(city)
            }
        }
    }

    itineraryViewModel.setFilters()
    val activity = LocalContext.current as Activity

    BackHandler {
        activity.finish()
    }
    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination

    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            headerText(
                size = 30,
                text = stringResource(id = R.string.home),
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        content = {
            Column {
                HomeContentSearch(
                    modifier = Modifier.padding(16.dp),
                    countries = countries,
                    cities = cities,
                    destinationCountry = destinationCountry,
                    destinationCity = destinationCity,
                    viewModel = viewModel,
                    itineraryViewModel = itineraryViewModel,
                    navigationController = navigationController
                )
                HomeContentRecommendedDestinations(
                    modifier = Modifier.padding(16.dp),
                    viewModel = viewModel,
                    itineraryViewModel = itineraryViewModel,
                    navigationController = navigationController
                )
            }
        },
        bottomBar = {
            BottomNav(
                currentDestination
            ) {
                viewModel.onIndexChange(
                    selectedIndex = it,
                    navigationController = navigationController
                )
            }
        }
    )
}

@Composable
fun HomeContentSearch(
    modifier: Modifier,
    countries: List<String>,
    cities: List<String>,
    destinationCountry: String,
    destinationCity: String,
    viewModel: HomeViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    Column(modifier = modifier) {
        TriplDropDownCountriesMenu(destinationCountry, viewModel, countries)
        Spacer(modifier = modifier)
        TriplDropDownCitiesMenu(destinationCity, viewModel, cities)
        Spacer(modifier = modifier)
        TriplButton(
            text = stringResource(id = R.string.search),
            buttonEnable = true
        ) { viewModel.onSearchTrip(navigationController, itineraryViewModel) }

    }
}

@Composable
fun HomeContentRecommendedDestinations(
    modifier: Modifier,
    viewModel: HomeViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        TriplRecommendedDestinations(modifier, viewModel, itineraryViewModel,navigationController)
        Spacer(modifier = Modifier.padding(35.dp))
    }
}

@Composable
fun TriplDropDownCountriesMenu(
    destination: String,
    viewModel: HomeViewModel,
    countries: List<String>
) {

    val expanded: Boolean by viewModel.expandedCountries.observeAsState(initial = false)
    val interactionSource: MutableInteractionSource by viewModel.interactionSource.observeAsState(
        initial = MutableInteractionSource()
    )

    Column(
        Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { viewModel.onExpandedCountriesChange(false) }
            )
    ) {
        Row(Modifier.fillMaxWidth()) {
            val icon: ImageVector = if (expanded) {
                Icons.Default.KeyboardArrowUp
            } else {
                Icons.Default.KeyboardArrowDown
            }
            TriplTextField(
                value = destination,
                label = stringResource(id = R.string.destination_country),
                type = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done,
                icon = icon,
                onClick = { viewModel.onExpandedCountriesChange(!expanded) }
            ) {
                viewModel.onSelectedCountryTextChange(it)
                viewModel.onExpandedCountriesChange(true)
            }
        }

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth(),
                elevation = 8.dp
            ) {
                LazyColumn(Modifier.heightIn(max = TextFieldDefaults.MinHeight * 4)) {
                    if (destination.isNotEmpty()) {
                        items(
                            countries.filter {
                                it.contains(destination, ignoreCase = true)
                            }
                                .sorted()
                        ) {
                            DestinationItems(text = it) { text ->
                                viewModel.onSelectedCountryTextChange(text)
                                viewModel.onExpandedCountriesChange(false)
                            }
                        }
                    } else {
                        items(
                            countries.sorted()
                        ) {
                            DestinationItems(text = it) { text ->
                                viewModel.onSelectedCountryTextChange(text)
                                viewModel.onExpandedCountriesChange(false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TriplDropDownCitiesMenu(destination: String, viewModel: HomeViewModel, cities: List<String>) {

    val expanded: Boolean by viewModel.expandedCities.observeAsState(initial = false)
    val interactionSource: MutableInteractionSource by viewModel.interactionSource.observeAsState(
        initial = MutableInteractionSource()
    )

    Column(
        Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { viewModel.onExpandedCountriesChange(false) }
            )
    ) {
        Row(Modifier.fillMaxWidth()) {
            val icon: ImageVector = if (expanded) {
                Icons.Default.KeyboardArrowUp
            } else {
                Icons.Default.KeyboardArrowDown
            }
            TriplTextField(
                label = stringResource(id = R.string.destination_city),
                type = KeyboardType.Text,
                value = destination,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done,
                icon = icon,
                onClick = { viewModel.onExpandedCitiesChange(!expanded) }
            ) {
                viewModel.onSelectedCityTextChange(it)
                viewModel.onExpandedCitiesChange(true)
            }
        }

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth(),
                elevation = 8.dp
            ) {
                LazyColumn(Modifier.heightIn(max = TextFieldDefaults.MinHeight * 4)) {
                    if (destination.isNotEmpty()) {
                        items(
                            cities.filter {
                                it.contains(destination, ignoreCase = true)
                            }
                                .sorted()
                        ) {
                            DestinationItems(text = it) { text ->
                                viewModel.onSelectedCityTextChange(text)
                                viewModel.onExpandedCitiesChange(false)
                            }
                        }
                    } else {
                        items(
                            cities.sorted()
                        ) {
                            DestinationItems(text = it) { text ->
                                viewModel.onSelectedCityTextChange(text)
                                viewModel.onExpandedCitiesChange(false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationItems(
    text: String,
    onSelect: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onSelect(text) }
            .padding(8.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

@Composable
fun TriplRecommendedDestinations(
    modifier: Modifier,
    viewModel: HomeViewModel,
    itineraryViewModel: ItineraryViewModel,
    navigationController: NavHostController
) {
    val recommendedFlags: List<Map<String, String>> by viewModel.suggestedFlags.observeAsState(
        initial = listOf()
    )
    Column {
        if (recommendedFlags.isNotEmpty()) {
            for (rf in recommendedFlags) {
                Column(Modifier.clickable {
                    viewModel.onSearchTrip(
                        navigationController,
                        itineraryViewModel,
                        viewModel.sanitizeFlagMap(rf.keys)
                    )
                }
                ) {
                    AsyncImage(
                        modifier = modifier.fillMaxWidth().height(180.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.sanitizeFlagMap(rf.values))
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = viewModel.sanitizeFlagMap(rf.keys),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Text(
                        text = viewModel.sanitizeFlagMap(rf.keys).uppercase(),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun BottomNav(
    currentDestination: NavDestination?,
    onIndexChange: (Int) -> Unit
) {
    BottomNavigation(elevation = 0.dp) {
        BottomNavigationItem(
            onClick = { onIndexChange(1) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo_vector),
                    contentDescription = stringResource(id = R.string.home),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            },
            label = { Text(text = stringResource(id = R.string.home)) },
            selected = currentDestination?.hierarchy?.any { it.route == Routes.HomeScreen.route } == true//index == 1
        )
        BottomNavigationItem(
            onClick = { onIndexChange(2) },
            icon = {
                Icon(
                    imageVector = Icons.Default.FlightTakeoff,
                    contentDescription = stringResource(id = R.string.trip)
                )
            },
            label = { Text(text = stringResource(id = R.string.trip)) },
            selected = currentDestination?.hierarchy?.any { it.route == Routes.TripScreen.route } == true
        )
        BottomNavigationItem(
            onClick = { onIndexChange(3) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.profile)
                )
            },
            label = { Text(text = stringResource(id = R.string.profile)) },
            selected = currentDestination?.hierarchy?.any { it.route == Routes.ProfileScreen.route } == true
        )
    }

    /*
@Composable
fun TriplDatePicker(viewModel: HomeViewModel) {
    val startDate: String by viewModel.startDate.observeAsState(initial = "")
    val endDate: String by viewModel.endDate.observeAsState(initial = "")
    val startYear: Int
    val startMonth: Int
    val startDay: Int
    val endYear: Int
    val endMonth: Int
    val endDay: Int
    val calendar = Calendar.getInstance()
    startYear = calendar.get(Calendar.YEAR)
    startMonth = calendar.get(Calendar.MONTH)
    startDay = calendar.get(Calendar.DAY_OF_MONTH)
    endYear = calendar.get(Calendar.YEAR)
    endMonth = calendar.get(Calendar.MONTH)
    endDay = calendar.get(Calendar.DAY_OF_MONTH)

    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, startYearDPD: Int, startMonthDPD: Int, startDayDPD: Int ->
            if (startDay < 10) {
                viewModel.onStartDateChange("0$startDayDPD/${startMonthDPD + 1}/$startYearDPD")
            } else {
                viewModel.onStartDateChange("$startDayDPD/${startMonthDPD + 1}/$startYearDPD")
            }
        }, startYear, startMonth, startDay
    )
    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, endYearDPD: Int, endMonthDPD: Int, endDayDPD: Int ->
            if (endDay < 10) {
                viewModel.onEndDateChange("0$endDayDPD/${endMonthDPD + 1}/$endYearDPD")
            } else {
                viewModel.onEndDateChange("$endDayDPD/${endMonthDPD + 1}/$endYearDPD")
            }
        }, endYear, endMonth, endDay
    )
    Box {
        Column {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(55.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TriplTextField(
                    label = stringResource(id = R.string.start_date),
                    type = KeyboardType.Text,
                    value = startDate,
                    modifier = Modifier.weight(0.5F),
                    icon = Icons.Filled.DateRange,
                    onClick = { startDatePickerDialog.show() },
                    imeAction = ImeAction.Done
                ) { viewModel.onStartDateChange(it) }
                Spacer(modifier = Modifier.padding(6.dp))
                TriplTextField(
                    label = stringResource(id = R.string.end_date),
                    type = KeyboardType.Text,
                    value = endDate,
                    modifier = Modifier.weight(0.5F),
                    icon = Icons.Filled.DateRange,
                    onClick = { endDatePickerDialog.show() },
                    imeAction = ImeAction.Done
                ) { viewModel.onEndDateChange(it) }
            }
        }
    }
}*/
}