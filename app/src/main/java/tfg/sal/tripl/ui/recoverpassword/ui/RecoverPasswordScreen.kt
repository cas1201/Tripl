package tfg.sal.tripl.ui.recoverpassword.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.ui.login.ui.TriplButton
import tfg.sal.tripl.ui.login.ui.TriplTextField
import tfg.sal.tripl.ui.login.ui.appLogo
import tfg.sal.tripl.ui.login.ui.headerText
import tfg.sal.tripl.ui.signup.ui.BackHeader

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    navigationController: NavHostController
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackHeader(Modifier.align(Alignment.TopStart)) {
            viewModel.onBackPressed(
                navigationController
            )
        }
        PasswordForgetBody(
            modifier = Modifier.align(Alignment.Center),
            viewModel = viewModel,
            navigationController = navigationController
        )
    }
}

@Composable
fun PasswordForgetBody(
    modifier: Modifier,
    viewModel: RecoverPasswordViewModel,
    navigationController: NavHostController
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val recoverPasswordEnable: Boolean by viewModel.recoverPasswordEnable.observeAsState(initial = false)

    Column(modifier = modifier) {
        appLogo(size = 80, modifier = Modifier.align(Alignment.CenterHorizontally))
        headerText(
            size = 19,
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(32.dp))

        Column(modifier = modifier) {
            TriplTextField(
                text = stringResource(id = R.string.email),
                type = KeyboardType.Email,
                value = email,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done
            ) {
                viewModel.onSignUpChange(it)
            }
            Spacer(modifier = Modifier.padding(16.dp))
            TriplButton(
                text = stringResource(id = R.string.recover_password),
                buttonEnable = recoverPasswordEnable
            ) { viewModel.onRecoverPasswordSelected(navigationController) }
        }
    }
}
