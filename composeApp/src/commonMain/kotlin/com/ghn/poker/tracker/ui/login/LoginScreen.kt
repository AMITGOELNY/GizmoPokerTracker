package com.ghn.poker.tracker.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.presentation.login.LoginActions
import com.ghn.poker.tracker.presentation.login.LoginEffects
import com.ghn.poker.tracker.presentation.login.LoginViewModel
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.title200
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = koinInject(),
    onSignInSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loginEffects.collect { effect ->
            when (effect) {
                LoginEffects.NavigateToDashboard -> onSignInSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "GiZMO POKER",
                        style = MaterialTheme.typography.title200.copy(color = Color(0xFFAFA21D))
                    )
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Login to Your Account",
                style = MaterialTheme.typography.title200,
                modifier = Modifier.padding(vertical = 16.dp),
            )
            FormBody(
                loading = state.value.authenticating,
                onUsernameChange = { viewModel.dispatch(LoginActions.OnUsernameChange(it)) },
                onPasswordChange = { viewModel.dispatch(LoginActions.OnPasswordChange(it)) },
                onSubmit = { viewModel.dispatch(LoginActions.OnSubmit) }
            )
        }
    }
}

@Composable
internal fun FormBody(
    loading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    var usernameValue by remember { mutableStateOf(TextFieldValue("")) }
    var passwordValue by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(24.dp))

        OutlineTextField(
            textFieldValue = usernameValue,
            onValueChange = {
                usernameValue = it
                onUsernameChange(it.text)
            },
            leadingIconId = "ic_message.xml",
            placeHolder = "Username",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
        )

//        ErrorText(
//            isError = model.loginError?.usernameError != null,
//            errorText = model.loginError?.usernameError?.name.orEmpty(),
//            label = "Username is ",
//        )
        Spacer(Modifier.height(24.dp))

        OutlineTextField(
            textFieldValue = passwordValue,
            onValueChange = {
                passwordValue = it
                onPasswordChange(it.text)
            },
            leadingIconId = "ic_lock.xml",
            placeHolder = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go,
            ),
            visualTransformation = PasswordVisualTransformation(),
            isError = false,
            readOnly = false
        )

//        ErrorText(
//            isError = model.loginError?.passwordError != null,
//            errorText = model.loginError?.passwordError?.name.orEmpty(),
//            label = "Password is",
//        )

        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            buttonText = "Sign In",
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            onClick = onSubmit,
            isEnabled = !loading,
            showLoading = loading
        )
    }
}

@Composable
private fun OutlineTextField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    leadingIconId: String?,
    placeHolder: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = textFieldValue,
        placeholder = {
            Text(
                text = placeHolder,
                style = MaterialTheme.typography.title200.copy(
                    letterSpacing = 0.2.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
                ),
                color = Color(0xFF9E9E9E),
            )
        },
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.title200.copy(
            letterSpacing = 0.2.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        leadingIcon = null,
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth().then(modifier),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFAFA21D),
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedContainerColor = if (isError) Color(0x14E21221) else Color(0xFF1F222A),
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
        ),
    )
}
