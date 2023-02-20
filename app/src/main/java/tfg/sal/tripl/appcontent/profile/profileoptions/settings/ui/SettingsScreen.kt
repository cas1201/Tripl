package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
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
