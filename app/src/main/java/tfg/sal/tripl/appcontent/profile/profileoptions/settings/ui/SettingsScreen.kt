package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.itinerary.data.POITypes
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.home.itinerary.ui.TypeCheckBox
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.TriplTextField
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.Divisor
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.BackHeader
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigationController: NavHostController
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            BackHeader { viewModel.onBackPressed(navigationController) }
        },
        content = { SettingsBody(viewModel) },
        bottomBar = {
            Box(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TriplButton(text = stringResource(R.string.save_preferences), buttonEnable = true) {
                    viewModel.savePreferences(context)
                }
            }
        }
    )
}

@Composable
fun BackHeader(onBackPressed: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = stringResource(id = R.string.back),
        modifier = Modifier.clickable { onBackPressed() }
    )
}

@Composable
fun SettingsBody(viewModel: SettingsViewModel) {
    val switchChecked: Boolean by viewModel.switchChecked.observeAsState(initial = false)
    val filters = listOf(R.string.amount, R.string.rating, R.string.type, R.string.distance)

    val poisAmount: String by viewModel.poisAmount.observeAsState(initial = "")
    val poisRating: Int by viewModel.poisRating.observeAsState(initial = -1)
    val poisDistance: Float by viewModel.poisDistance.observeAsState(initial = 25f)

    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.mode_choice),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 23.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = stringResource(id = R.string.light_mode)
            )
            Switch(
                checked = switchChecked,
                onCheckedChange = { viewModel.onSwitchChange(!switchChecked) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.surface,
                    checkedTrackColor = MaterialTheme.colors.onSurface,
                    uncheckedThumbColor = MaterialTheme.colors.secondaryVariant,
                    uncheckedTrackColor = MaterialTheme.colors.secondaryVariant,
                )
            )
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = stringResource(id = R.string.dark_mode)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.padding(18.dp))
        Text(
            text = stringResource(id = R.string.search_filters),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 23.sp
        )
        Spacer(modifier = Modifier.padding(16.dp))
        filters.forEach { filter ->
            Column {
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
                                RatingRadioButton(
                                    ratingLvl = r,
                                    poisRating = poisRating,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                    R.string.type -> {
                        TypeCheckBox(POITypes.values().toList(), viewModel)
                    }
                    R.string.distance -> {
                        Box {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Slider(
                                    modifier = Modifier.width(220.dp).height(10.dp),
                                    value = poisDistance,
                                    valueRange = 1f..9999f,
                                    onValueChange = {
                                        viewModel.onDistanceChange(it)
                                    }
                                )
                                Spacer(modifier = Modifier.padding(4.dp).weight(1f))
                                Text(text = poisDistance.toInt().toString() + " Km")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Divisor()
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 14.dp))
    }
}

@Composable
fun RatingRadioButton(ratingLvl: Int, poisRating: Int, viewModel: SettingsViewModel) {
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
fun TypeCheckBox(types: List<POITypes>, viewModel: SettingsViewModel) {
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
