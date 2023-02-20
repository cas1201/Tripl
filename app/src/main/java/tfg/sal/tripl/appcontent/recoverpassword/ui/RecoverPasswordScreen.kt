package tfg.sal.tripl.appcontent.recoverpassword.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.data.network.FireBaseAuthResource
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.TriplTextField
import tfg.sal.tripl.appcontent.login.ui.appLogo
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.signup.ui.BackHeader
import tfg.sal.tripl.theme.SecondaryColor

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val recoverPasswordFlow = fireBaseViewModel?.recoverPasswordFlow?.collectAsState()

        BackHeader(Modifier.align(Alignment.TopStart)) {
            viewModel.onBackPressed(
                navigationController
            )
        }
        PasswordForgetBody(
            modifier = Modifier.align(Alignment.TopCenter),
            viewModel = viewModel,
            fireBaseViewModel = fireBaseViewModel,
            recoverPasswordFlow = recoverPasswordFlow,
            navigationController = navigationController
        )
    }
}


@Composable
fun PasswordForgetBody(
    modifier: Modifier,
    viewModel: RecoverPasswordViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    recoverPasswordFlow: State<FireBaseAuthResource<Void?>?>?,
    navigationController: NavHostController
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val recoverPasswordEnable: Boolean by viewModel.recoverPasswordEnable.observeAsState(initial = false)
    val recoverPasswordPressed: Boolean by viewModel.recoverPasswordPressed.observeAsState(initial = false)

    Column(modifier = modifier.padding(top = 100.dp)) {
        appLogo(size = 80, modifier = Modifier.align(Alignment.CenterHorizontally))
        headerText(
            size = 19,
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(32.dp))

        Column(modifier = modifier) {
            TriplTextField(
                label = stringResource(id = R.string.email),
                type = KeyboardType.Email,
                value = email,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done
            ) {
                viewModel.onRecoverPasswordChange(it)
            }
            Spacer(modifier = Modifier.padding(16.dp))
            TriplButton(
                text = stringResource(id = R.string.recover_password),
                buttonEnable = recoverPasswordEnable
            ) { fireBaseViewModel?.recoverPassword(email) }
        }

        recoverPasswordFlow?.value?.let {
            when (it) {
                is FireBaseAuthResource.Error -> {
                    if (recoverPasswordPressed) {
                        viewModel.showToast(LocalContext.current, R.string.recover_password_error)
                    }
                }
                FireBaseAuthResource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SecondaryColor)
                    }
                }
                is FireBaseAuthResource.Success -> {
                    if (recoverPasswordPressed) {
                        viewModel.showToast(LocalContext.current, R.string.recover_password_success)
                    }
                    LaunchedEffect(Unit) {
                        viewModel.onRecoverPasswordSelected(navigationController)
                    }
                }
            }
        }
    }
}
