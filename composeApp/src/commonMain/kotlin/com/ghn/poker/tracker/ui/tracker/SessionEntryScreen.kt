package com.ghn.poker.tracker.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.ui.theme.Dimens

@Composable
fun SessionEntryScreen() {
    var value by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(Dimens.grid_2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_2)
    ) {
        Text("Enter session Data")
        InputRow("Date") {
            TextEntryField(value) { newText ->
                //                    onSearch(newText)
                value = newText
            }
        }

        InputRow("BuyIn Amount") {
            TextEntryField(value) { newText ->
                //                    onSearch(newText)
                value = newText
            }
        }
    }
}

@Composable
private fun InputRow(label: String, content: @Composable () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
//                textStyle = MaterialTheme.typography.body2.copy(
//                    lineHeight = 20.sp,
//                    fontFamily = BeVietnamPro,
//                    color = MaterialTheme.colors.blackForTextNewColor
//                ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .width(120.dp)
                    .border(1.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium)
                    .background(Color(0xffF6F6F6), MaterialTheme.shapes.medium)
                    .padding(
                        vertical = Dimens.grid_0_5,
                        horizontal = Dimens.grid_1
                    ), // inner padding,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    innerTextField()
                }
            }
        }
    )
}
