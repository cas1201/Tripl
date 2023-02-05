package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.BackHeader
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            BackHeader { viewModel.onBackPressed(navigationController) }
        },
        content = { SettingsBody(viewModel) }
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
    val language: String by viewModel.language.observeAsState(initial = Locale.getDefault().displayLanguage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(
            text = stringResource(id = R.string.language_choice),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 23.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        for (l in listOf(R.string.spanish, R.string.english)) {
            languageRadioButton(selectedLanguage = stringResource(l), currentLanguage= language, viewModel = viewModel)
        }
        Spacer(modifier = Modifier.padding(16.dp))
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
    }
}

@Composable
fun languageRadioButton(selectedLanguage: String, currentLanguage: String, viewModel: SettingsViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedLanguage.lowercase() == currentLanguage.lowercase(),
            onClick = { viewModel.onLanguageChange(selectedLanguage) }
        )
        Text(text = selectedLanguage)
    }
}
