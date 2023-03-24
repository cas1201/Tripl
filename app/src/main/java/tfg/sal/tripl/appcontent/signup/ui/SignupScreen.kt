package tfg.sal.tripl.appcontent.signup.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.google.firebase.auth.FirebaseUser
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.data.network.FireBaseAuthResource
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.*
import tfg.sal.tripl.theme.SecondaryColor

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val signupFlow = fireBaseViewModel?.signupFlow?.collectAsState()

        BackHeader(Modifier.align(Alignment.TopStart)) {
            viewModel.onBackPressed(
                navigationController
            )
        }
        SignUpBody(
            Modifier.align(Alignment.Center),
            viewModel,
            fireBaseViewModel,
            signupFlow,
            navigationController
        )
    }
}

@Composable
fun BackHeader(modifier: Modifier, onBackPressed: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = stringResource(id = R.string.back),
        modifier = modifier.clickable { onBackPressed() }
    )
}

@Composable
fun SignUpBody(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    signupFlow: State<FireBaseAuthResource<FirebaseUser>?>?,
    navigationController: NavHostController
) {
    val name: String by viewModel.name.observeAsState(initial = "")
    val surname: String by viewModel.surname.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val validEmail: Boolean by viewModel.validEmail.observeAsState(initial = false)
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordLength: Boolean by viewModel.passwordLength.observeAsState(initial = false)
    val passwordsMatch: Boolean by viewModel.passwordsMatch.observeAsState(initial = false)
    val passwordRepeat: String by viewModel.passwordRepeat.observeAsState(initial = "")
    val passwordVisible: Boolean by viewModel.passwordVisible.observeAsState(initial = false)
    val passwordRepeatVisible: Boolean by viewModel.passwordRepeatVisible.observeAsState(initial = false)
    val signUpEnable: Boolean by viewModel.signupEnable.observeAsState(initial = false)
    val signUpPressed: Boolean by viewModel.signUpPressed.observeAsState(initial = false)

    val context = LocalContext.current

    Column(modifier = modifier) {
        appLogo(size = 80, modifier = Modifier.align(Alignment.CenterHorizontally))
        headerText(
            size = 19,
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(32.dp))

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            TriplTextField(
                label = stringResource(id = R.string.name),
                type = KeyboardType.Text,
                value = name,
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.onSignUpChange(
                    it,
                    surname,
                    email,
                    password,
                    passwordRepeat
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            TriplTextField(
                label = stringResource(id = R.string.surname),
                type = KeyboardType.Text,
                value = surname,
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.onSignUpChange(
                    name,
                    it,
                    email,
                    password,
                    passwordRepeat
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            TriplTextField(
                label = stringResource(id = R.string.email),
                type = KeyboardType.Email,
                value = email,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done
            ) {
                viewModel.onSignUpChange(
                    name,
                    surname,
                    it,
                    password,
                    passwordRepeat
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(
                text = stringResource(id = R.string.password),
                password = password,
                passwordVisible = passwordVisible,
                onTextFieldChange = {
                    viewModel.onSignUpChange(
                        name,
                        surname,
                        email,
                        it,
                        passwordRepeat
                    )
                },
                onPasswordVisibleChange = {
                    viewModel.onPasswordVisibleChange(
                        passwordVisible
                    )
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(
                text = stringResource(id = R.string.repeat_password),
                password = passwordRepeat,
                passwordVisible = passwordRepeatVisible,
                onTextFieldChange = {
                    viewModel.onSignUpChange(
                        name,
                        surname,
                        email,
                        password,
                        it
                    )
                },
                onPasswordVisibleChange = {
                    viewModel.onPasswordRepeatVisibleChange(
                        passwordRepeatVisible
                    )
                }
            )
            Spacer(modifier = Modifier.padding(16.dp))
            TriplButton(
                text = stringResource(id = R.string.signup),
                buttonEnable = true
            ) {
                if (signUpEnable) {
                    fireBaseViewModel?.signup("$name $surname", email, password)
                } else {
                    if (viewModel.checkValues()) {
                        if (!passwordLength) {
                            viewModel.showToast(context, R.string.password_length)
                        } else {
                            if (!passwordsMatch) {
                                viewModel.showToast(context, R.string.paswords_matching)
                            } else {
                                if (!validEmail) {
                                    viewModel.showToast(context, R.string.invalid_email)
                                }
                            }
                        }
                    }
                }
            }
        }

        signupFlow?.value?.let {
            when (it) {
                is FireBaseAuthResource.Error -> {
                    if (signUpPressed) {
                        viewModel.showToast(context, R.string.signup_error)
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
                    LaunchedEffect(Unit) {
                        viewModel.onSignUpSelected(context, navigationController)
                    }
                }
            }
        }
    }
}