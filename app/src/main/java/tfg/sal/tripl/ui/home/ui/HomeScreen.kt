package tfg.sal.tripl.ui.home.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.model.Routes
import tfg.sal.tripl.ui.login.ui.TriplTextField
import tfg.sal.tripl.ui.login.ui.headerText

@Composable
fun HomeScreen(viewModel: HomeViewModel, navigationController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            headerText(
                size = 30,
                text = stringResource(id = R.string.home),
                modifier = Modifier.padding(16.dp)
            )
        },
        content = { HomeContent(modifier = Modifier.padding(16.dp), viewModel) },
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
fun HomeContent(modifier: Modifier, viewModel: HomeViewModel) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        TriplDropDownMenu(viewModel)
        TriplDatePicker(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TriplDropDownMenu(viewModel: HomeViewModel) {

    val selectedText: String by viewModel.selectedText.observeAsState(initial = "")
    val expanded: Boolean by viewModel.expanded.observeAsState(initial = false)
    val places = listOf(
        "Europa",
        "España",
        "Austria",
        "Polonia",
        "Alemania",
        "Francia",
        "Italia",
        "República Checa",
        "Portugal",
        "Reino Unido",
        "Dinamarca",
        "Suecia",
        "Finlandia",
        "Suiza",
        "Grecia",
        "Turquia",
        "Croacia",
        "Ucrania",
        "Serbia",
        "Malta",
        "Bulgaria",
        "Rumania",
        "Rusia",
        "Bielorusia",
        "Lituania",
        "Estonia",
        "Holanda",
        "Irlanda",
        "Letonia"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { viewModel.onExpandedChange(!expanded) }) {
        TriplTextField(
            text = stringResource(id = R.string.destination),
            type = KeyboardType.Text,
            value = selectedText,
            modifier = Modifier
                .clickable { viewModel.onExpandedChange(true) }
                .fillMaxWidth(),
            imeAction = ImeAction.Done
        ) { viewModel.onSelectedTextChange(it) }
        val filterPlaces = places.filter { it.contains(selectedText, ignoreCase = true) }
        if (filterPlaces.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { viewModel.onExpandedChange(false) }) {
                filterPlaces.forEach { place ->
                    DropdownMenuItem(onClick = {
                        viewModel.onSelectedTextChange(place)
                        viewModel.onExpandedChange(false)
                    }) {
                        Text(text = place)
                    }
                }
            }
        } else {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { viewModel.onExpandedChange(false) }) {
                Text(
                    text = stringResource(id = R.string.destination_not_found),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

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
        { _, startYear: Int, startMonth: Int, startDay: Int ->
            viewModel.onStartDateChange("$endDay/${endMonth + 1}/$endYear")
        }, startYear, startMonth, startDay
    )
    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, endYear: Int, endMonth: Int, endDay: Int ->
            viewModel.onEndDateChange("$endDay/${endMonth + 1}/$endYear")
        }, endYear, endMonth, endDay
    )
    Box {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp)
                .height(55.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TriplTextField(
                text = stringResource(id = R.string.start_date),
                type = KeyboardType.Text,
                value = startDate,
                modifier = Modifier.weight(0.5F),
                icon = Icons.Filled.DateRange,
                onDateClick = startDatePickerDialog,
                imeAction = ImeAction.Done,
                onTextFieldChange = { viewModel.onStartDateChange(it) }
            )
            Spacer(modifier = Modifier.padding(6.dp))
            TriplTextField(
                text = stringResource(id = R.string.end_date),
                type = KeyboardType.Text,
                value = endDate,
                modifier = Modifier.weight(0.5F),
                icon = Icons.Filled.DateRange,
                onDateClick = endDatePickerDialog,
                imeAction = ImeAction.Done,
                onTextFieldChange = { viewModel.onEndDateChange(it) }
            )
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
}