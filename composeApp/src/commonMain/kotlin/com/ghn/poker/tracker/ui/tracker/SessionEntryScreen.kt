package com.ghn.poker.tracker.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.presentation.session.SessionEntryAction
import com.ghn.poker.tracker.presentation.session.SessionEntryEffect
import com.ghn.poker.tracker.presentation.session.SessionEntryViewModel
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.generated.resources.Res
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SessionEntryScreen(
    onBackClick: () -> Unit,
    viewModel: SessionEntryViewModel = koinInject()
) {
    var startAmount by remember { mutableStateOf("") }
    var endAmount by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState().value
    var dateTextField by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(state.dateFormatted) {
        dateTextField = TextFieldValue(state.dateFormatted)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SessionEntryEffect.OnSessionCreated -> onBackClick()
            }
        }
    }

    if (showDatePicker) {
        SimpleDateRangePickerInDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { viewModel.dispatch(SessionEntryAction.UpdateDate(it)) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.create_session),
                        color = MaterialTheme.colorScheme.onBackground,
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
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = Dimens.grid_2_5)
                .padding(bottom = Dimens.grid_2_5)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_3)
        ) {
            InputRow(stringResource(Res.string.create_session_date)) {
                ReadonlyTextField(
                    value = dateTextField,
                    onValueChange = { },
                    onClick = { showDatePicker = true },
                    label = {
                    },
                    modifier = Modifier.width(200.dp)
                )
            }

            InputRow(stringResource(Res.string.create_session_enter_amount)) {
                TextEntryField(startAmount) { amount ->
                    viewModel.dispatch(SessionEntryAction.UpdateStartAmount(amount.toDoubleOrNull()))
                    startAmount = amount
                }
            }

            InputRow(stringResource(Res.string.create_session_end_amount)) {
                TextEntryField(endAmount) { amount ->
                    viewModel.dispatch(SessionEntryAction.UpdateEndAmount(amount.toDoubleOrNull()))
                    endAmount = amount
                }
            }

            InputRow(stringResource(Res.string.create_session_game_type)) {
                ExpandedDropDown(GameType.entries, state = state.gameType) {
                    viewModel.dispatch(SessionEntryAction.UpdateGameType(it))
                }
            }

            InputRow(stringResource(Res.string.create_session_location_type)) {
                ExpandedDropDown(Venue.entries, state.venue) {
                    viewModel.dispatch(SessionEntryAction.UpdateVenue(it))
                }
            }

            Spacer(Modifier.weight(1f))
            PrimaryButton(
                buttonText = stringResource(Res.string.create_session_save),
                isEnabled = !state.isCreatingSession,
                showLoading = state.isCreatingSession,
                onClick = { viewModel.dispatch(SessionEntryAction.SaveSession) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private inline fun <T : Enum<T>> ExpandedDropDown(
    items: List<T>,
    state: T,
    crossinline onSelected: (T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newValue -> isExpanded = newValue },
        modifier = Modifier.width(200.dp)
    ) {
        TextField(
            value = state.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.toString(), color = Color.White) },
                    onClick = {
                        onSelected(selectionOption)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun InputRow(
    label: String,
    content: @Composable () -> Unit
) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.titleMedium)
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
                modifier = Modifier
                    .width(200.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                    .background(Color(0xffF6F6F6), MaterialTheme.shapes.medium)
                    .padding(vertical = Dimens.grid_1_5, horizontal = Dimens.grid_2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box {
                    innerTextField()
                }
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
private fun SimpleDateRangePickerInDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Instant) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )
    DatePickerDialog(
        shape = RoundedCornerShape(6.dp),
        onDismissRequest = onDismiss,
        confirmButton = {
            PrimaryButton(
                buttonText = stringResource(Res.string.confirm),
                fillMaxWidth = false,
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(Instant.fromEpochMilliseconds(it))
                    }
                    onDismiss()
                }
            )
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF1F222A)
        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ReadonlyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.CenterStart) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            leadingIcon = { Icon(Icons.Rounded.DateRange, null) },
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}
