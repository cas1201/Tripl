package tfg.sal.tripl.ui.login.ui

import android.app.Activity
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R


@Composable
fun LoginScreen(viewModel: LoginViewModel, navigationController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CloseHeader(Modifier.align(Alignment.TopEnd))
        LoginBody(Modifier.align(Alignment.Center), viewModel, navigationController)
        SignUpFooter(Modifier.align(Alignment.BottomCenter), viewModel, navigationController)
    }
}

@Composable
fun CloseHeader(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = stringResource(id = R.string.close),
        modifier = modifier.clickable { activity.finish() }
    )
}

@Composable
fun LoginBody(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navigationController: NavHostController
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordVisible: Boolean by viewModel.passwordVisible.observeAsState(initial = false)
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = modifier) {
            appLogo(size = 125, modifier = Modifier.align(Alignment.CenterHorizontally))
            headerText(
                size = 30,
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(32.dp))
            TriplTextField(
                text = stringResource(id = R.string.email),
                type = KeyboardType.Email,
                value = email,
                modifier = Modifier.fillMaxWidth()
            ) { viewModel.onLoginChange(it, password) }
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(
                text = stringResource(id = R.string.password),
                password = password,
                passwordVisible = passwordVisible,
                onTextFieldChange = { viewModel.onLoginChange(email, it) },
                onPasswordVisibleChange = { viewModel.onPasswordVisibleChange(passwordVisible) })
            Spacer(modifier = Modifier.padding(8.dp))
            ForgotPassword(
                viewModel = viewModel,
                navigationController = navigationController,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            TriplButton(
                text = stringResource(id = R.string.login),
                buttonEnable = loginEnable
            ) { viewModel.onLoginSelected(navigationController) }
            Spacer(modifier = Modifier.padding(12.dp))
            LoginDivider()
            Spacer(modifier = Modifier.padding(12.dp))
            SocialLogin()
        }
    }
}

@Composable
fun SignUpFooter(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navigationController: NavHostController
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(20.dp))
        SignUp { viewModel.onSignUpPressed(navigationController) }
        Spacer(modifier = Modifier.size(6.dp))
    }
}


@Composable
fun appLogo(size: Int, modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = stringResource(id = R.string.logo),
        modifier = modifier.size(size.dp)
    )
}

@Composable
fun headerText(size: Int, text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = size.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00897b)
    )
}

@Composable
fun TriplTextField(
    text: String,
    type: KeyboardType,
    value: String,
    modifier: Modifier,
    icon: ImageVector? = null,
    onDateClick: DatePickerDialog? = null,
    imeAction: ImeAction = ImeAction.Next,
    onTextFieldChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = { onTextFieldChange(it) },
        modifier = modifier,
        label = { Text(text = text) },
        placeholder = { Text(text = text) },
        keyboardOptions = KeyboardOptions(keyboardType = type, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
            onDone = { focusManager.clearFocus() }),
        singleLine = true,
        trailingIcon = {
            if (icon != null) {
                IconButton(onClick = { onDateClick?.show() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = R.string.select_date)
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordField(
    text: String,
    password: String,
    passwordVisible: Boolean,
    onTextFieldChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = password,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = text) },
        placeholder = { Text(text = text) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            val visibleIcon = if (passwordVisible)
                Icons.Filled.VisibilityOff
            else Icons.Filled.Visibility

            val description =
                if (passwordVisible) stringResource(id = R.string.hide_password)
                else stringResource(id = R.string.show_password)

            IconButton(onClick = { onPasswordVisibleChange(passwordVisible) }) {
                Icon(imageVector = visibleIcon, contentDescription = description)
            }
        }
    )
}

@Composable
fun ForgotPassword(
    viewModel: LoginViewModel,
    navigationController: NavHostController,
    modifier: Modifier
) {
    Text(
        text = stringResource(id = R.string.forgot_password),
        modifier = modifier.clickable { viewModel.onPasswordForget(navigationController) },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TriplButton(text: String, buttonEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF00897b)
        ),
        enabled = buttonEnable
    ) {
        Text(text = text)
    }
}

@Composable
fun LoginDivider() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Divider(
            Modifier
                .height(1.dp)
                .weight(1f)
        )
        Text(
            text = stringResource(id = R.string.signin_with),
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(
            Modifier
                .height(1.dp)
                .weight(1f)
        )
    }
}

@Composable
fun SocialLogin() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = stringResource(id = R.string.social_login_google),
            modifier = Modifier
                .size(25.dp)
                .clickable { }
        )
        Spacer(modifier = Modifier.width(30.dp))
        Image(
            painter = painterResource(id = R.drawable.fb),
            contentDescription = stringResource(id = R.string.social_login_facebook),
            modifier = Modifier
                .size(25.dp)
                .clickable { }
        )
    }
}

@Composable
fun SignUp(onSignUpPressed: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(id = R.string.no_account),
            fontSize = 12.sp
        )
        Text(
            text = stringResource(id = R.string.signup),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable { onSignUpPressed() },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4EABE9)
        )
    }
}