package com.ghn.poker.tracker.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.presentation.session.SessionEntryAction
import com.ghn.poker.tracker.presentation.session.SessionEntryViewModel
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEntryScreen(
    onBackClick: () -> Unit,
    viewModel: SessionEntryViewModel = SessionEntryViewModel()
) {
    var startAmount by remember { mutableStateOf("") }
    var endAmount by remember { mutableStateOf("") }
    val state = viewModel.state.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Create Session",
//                        style = MaterialTheme.typography.p3.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .padding(horizontal = Dimens.grid_2_5)
                    .padding(bottom = Dimens.grid_2_5)
                    .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
        ) {
            InputRow("Date") {
                Text(state.date.date.toString())
            }

            InputRow("BuyIn Amount") {
                TextEntryField(startAmount) { amount ->
                    viewModel.dispatch(SessionEntryAction.UpdateStartAmount(amount.toDoubleOrNull()))
                    startAmount = amount
                }
            }

            InputRow("End Amount") {
                TextEntryField(endAmount) { amount ->
                    viewModel.dispatch(SessionEntryAction.UpdateEndAmount(amount.toDoubleOrNull()))
                    endAmount = amount
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                buttonText = "Save Session",
                isEnabled = state.saveEnabled,
                onClick = { viewModel.dispatch(SessionEntryAction.SaveSession) }
            )
        }
    }
}

@Composable
private fun InputRow(
    label: String,
    content: @Composable () -> Unit
) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(label)
        content()
    }
}

@Composable
private fun TextEntryField(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        decorationBox = { innerTextField ->
            Row(
                modifier =
                    Modifier
                        .width(120.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                        .background(Color(0xffF6F6F6), MaterialTheme.shapes.medium)
                        .padding(vertical = Dimens.grid_0_5, horizontal = Dimens.grid_1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box {
                    innerTextField()
                }
            }
        }
    )
}
