package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.BackHeader
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.SupportBoddy
import tfg.sal.tripl.theme.PrimaryTextColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    fireBaseViewModel: FireBaseViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = { BackHeader { viewModel.onBackPressed(navigationController) } },
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
    val supportEmail = stringResource(R.string.support_email)
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Settings", textAlign = TextAlign.Center)
        }
    }
}