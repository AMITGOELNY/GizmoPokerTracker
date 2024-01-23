package com.ghn.poker.tracker.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.presentation.login.CreateAccountActions
import com.ghn.poker.tracker.presentation.login.CreateAccountViewModel
import com.ghn.poker.tracker.ui.theme.title200
import gizmopoker.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpScreen(
    viewModel: CreateAccountViewModel = koinInject(),
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.title200.copy(color = Color(0xFFAFA21D))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.create_your_account),
                style = MaterialTheme.typography.title200,
                modifier = Modifier.padding(vertical = 16.dp),
            )
            FormBody(
                loading = state.value.authenticating,
                onUsernameChange = { viewModel.dispatch(CreateAccountActions.OnUsernameChange(it)) },
                onPasswordChange = { viewModel.dispatch(CreateAccountActions.OnPasswordChange(it)) },
                onSubmit = { viewModel.dispatch(CreateAccountActions.OnSubmit) }
            )
        }
    }
}
