package com.ghn.poker.tracker.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.ui.theme.Dimens

@Composable
fun CardsScreen() {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("5-card", "Hold Em Game", "Equity Calc")
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
                        text = "GiZMO Poker Trainer",
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            )
        }
    ) { padding ->

        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.grid_1),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabIndex])
                            .height(Dimens.grid_0_5)
                            .background(color = Color(0xFFFF6861))
                    )
                },
                divider = {
                    Box(
                        modifier = Modifier
                            .height(Dimens.grid_0_25)
                            .background(color = Color(0xFF2A2B39))
                    )
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                        },
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
//                            onTabItemClick(index)
                        }
                    )
                }
            }

            when (tabIndex) {
                0 -> FiveCardGame()
                1 -> HoldemGame()
                2 -> EquityCalculator()
            }
        }
    }
}
