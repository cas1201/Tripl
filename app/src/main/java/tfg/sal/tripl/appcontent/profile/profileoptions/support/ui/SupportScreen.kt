package tfg.sal.tripl.appcontent.profile.profileoptions.support.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import tfg.sal.tripl.theme.PrimaryTextColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SupportScreen(
    viewModel: SupportViewModel,
    fireBaseViewModel: FireBaseViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = { BackHeader { viewModel.onBackPressed(navigationController) } },
        content = { SupportBoddy(viewModel) }
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
fun SupportBoddy(viewModel: SupportViewModel) {
    val supportEmail = stringResource(R.string.support_email)
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(id = R.string.support_content),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            Text(
                text = supportEmail,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = PrimaryTextColor,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(horizontal = 20.dp).clickable { viewModel.sendEmail(supportEmail) }
            )
        }
    }
}