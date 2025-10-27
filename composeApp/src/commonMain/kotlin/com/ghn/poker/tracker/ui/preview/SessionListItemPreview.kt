package com.ghn.poker.tracker.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.tracker.SessionListItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Preview
@Composable
private fun SessionListItem_Preview() = SurfacePreview {
    Column(Modifier.padding(vertical = Dimens.grid_2)) {
        SessionListItem(
            SessionData(Clock.System.now(), "2000", "4000", Venue.HARD_ROCK_FL)
        )
    }
}
